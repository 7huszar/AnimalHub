package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.FrontendProperties;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.PaypalProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalExperienceContextDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalPaymentSourceDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalPaypalDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.DonationType;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.PaypalException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.PaypalService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.conflict.PaypalConflict;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.Amount;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.AuthResponseDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.CreateOrderDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.Intent;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalLink;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalPlaceOrderResponseDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PurchaseUnits;
import at.ac.tuwien.sepr.groupphase.backend.service.validator.PaypalValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PaypalProperties paypal;
    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;
    private final FrontendProperties frontend;
    private final PaypalConflict paypalConflict;
    private final PaypalValidator paypalValidator;
    private final UserService userService;

    private String retrieveAccessToken(String clientId, String clientSecret) throws PaypalException {
        LOGGER.trace("retrieveAccessToken({}, *)", clientId);
        WebClient client = WebClient.create(paypal.getSandbox());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "client_credentials");

        AuthResponseDto reply = client.post()
            .uri(paypal.getAuth())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes()))
            .body(BodyInserters.fromFormData(data))
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new PaypalException(clientResponse)))
            .bodyToMono(AuthResponseDto.class)
            .block();

        if (reply == null || reply.accessToken() == null) {
            throw new RuntimeException("TODO what should I do");
        }

        return reply.accessToken();
    }

    @Override
    public PaypalLink placeOrder(UUID id, PaypalDonationDto donationDto) throws NotFoundException, ValidationException, ConflictException, PaypalException, NotAuthorizedException {
        LOGGER.trace("placeOrder({}, {})", id, donationDto);

        paypalValidator.validateDonationOrder(id, donationDto);
        paypalConflict.conflictForPlacingOrder(id, donationDto);

        Shelter receiver = shelterRepository.findById(id).orElseThrow(NotFoundException::new);
        ApplicationUser donator = userRepository.findById(donationDto.donator()).orElseThrow(NotFoundException::new);

        if (!this.userService.getCurrentUser().getId().equals(donator.getId())) {
            throw new NotAuthorizedException("No permission for user1 " + this.userService.getCurrentUser().getId() + " to perform action on user2 " + donator.getId());
        }

        WebClient client = WebClient.create(paypal.getSandbox());

        CreateOrderDto orderDto = new CreateOrderDto(
            Intent.CAPTURE,
            List.of(new PurchaseUnits(
                new Amount(
                    "EUR",
                    donationDto.amount()))),
            new PaypalPaymentSourceDto(
                new PaypalPaypalDto(
                    new PaypalExperienceContextDto(
                        "IMMEDIATE_PAYMENT_REQUIRED",
                        "PAY_NOW",
                        frontend.getUrl() + "/donation/success",
                        frontend.getUrl() + "/donation/failure"
                    )
                )
            )
        );

        PaypalPlaceOrderResponseDto reply = client.post()
            .uri(paypal.getPlaceOrder())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + this.retrieveAccessToken(receiver.getPaypalClientId(), receiver.getPaypalSecret()))
            .bodyValue(orderDto)
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new PaypalException(clientResponse)))
            .bodyToMono(PaypalPlaceOrderResponseDto.class)
            .block();

        if (reply == null || reply.links() == null || reply.links().isEmpty()) {
            throw new RuntimeException("oh man");
        }

        Donation donation = Donation.builder()
            .user(donator)
            .amount(new BigDecimal(donationDto.amount()))
            .stamp(null)
            .payed(false)
            .id(reply.id())
            .shelter(receiver)
            .type(DonationType.ONE_TIME)
            .receiver(donationDto.receiver())
            .build();

        donationRepository.save(donation);

        PaypalLink linkForPayer = null;
        for (PaypalLink link : reply.links()) {
            if ("payer-action".equals(link.rel())) {
                linkForPayer = link;
            }
        }

        if (linkForPayer == null) {
            throw new RuntimeException("pay link not found");
        }

        return linkForPayer;
    }

    @Override
    public void captureOrder(String id) throws NotFoundException, ValidationException, ConflictException, PaypalException {
        LOGGER.trace("captureOrder({})", id);

        paypalValidator.validateCaptureOrder(id);
        paypalConflict.conflictForCaptureOrder(id);

        WebClient client = WebClient.create(paypal.getSandbox());

        Donation donation = donationRepository.findById(id).orElseThrow(NotFoundException::new);

        client.post()
            .uri(paypal.captureOrder(id))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + this.retrieveAccessToken(donation.getShelter().getPaypalClientId(), donation.getShelter().getPaypalSecret()))
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new PaypalException(clientResponse)))
            .bodyToMono(String.class)
            .block();

        donation.setStamp(LocalDateTime.now())
            .setPayed(true);

        donationRepository.save(donation);
    }
}
