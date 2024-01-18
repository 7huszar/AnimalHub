package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.PaypalProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalCaptureDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.DonationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.PaypalService;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.Amount;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.AuthResponseDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.CreateOrderDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.Intent;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalLink;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalPlaceOrderResponseDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PurchaseUnits;
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

    private String retrieveAccessToken(String clientId, String clientSecret) {
        WebClient client = WebClient.create(paypal.getSandbox());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "client_credentials");

        AuthResponseDto reply = client.post()
            .uri(paypal.getAuth())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes()))
            .body(BodyInserters.fromFormData(data))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("4xx, will handle it later - toke")))
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("5xx, will handle it later")))
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("is error")))
            .bodyToMono(AuthResponseDto.class)
            .block();

        if (reply == null || reply.accessToken() == null) {
            throw new RuntimeException("TODO what should I do");
        }

        return reply.accessToken();
    }

    @Override
    public PaypalLink placeOrder(UUID id, PaypalDonationDto donationDto) throws NotFoundException {
        LOGGER.trace("placeOrder({}, {})", id, donationDto);

        Shelter receiver = shelterRepository.findById(id).orElseThrow(NotFoundException::new);
        ApplicationUser donator = userRepository.findById(donationDto.donator()).orElseThrow(NotFoundException::new);

        WebClient client = WebClient.create(paypal.getSandbox());

        CreateOrderDto orderDto = new CreateOrderDto(
            Intent.CAPTURE,
            List.of(new PurchaseUnits(
                new Amount(
                    "EUR",
                    donationDto.amount())))
        );

        PaypalPlaceOrderResponseDto reply = client.post()
            .uri(paypal.getPlaceOrder())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + this.retrieveAccessToken(paypal.getClientId(), paypal.getClientSecret()))
            .bodyValue(orderDto)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException(clientResponse.toString())))
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException(clientResponse.toString())))
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("is error")))
            .bodyToMono(PaypalPlaceOrderResponseDto.class)
            .block();

        if (reply == null || reply.links() == null || reply.links().isEmpty()) {
            throw new RuntimeException("oh man");
        }

        Donation donation = Donation.builder()
            .user(donator)
            .stamp(null)
            .payed(false)
            .id(reply.id())
            .shelter(receiver)
            .build();

        donationRepository.save(donation);

        PaypalLink linkForPayer = null;
        for (PaypalLink link : reply.links()) {
            if ("approve".equals(link.rel())) {
                linkForPayer = link;
            }
        }

        if (linkForPayer == null) {
            throw new RuntimeException("pay link not found");
        }

        return linkForPayer;
    }

    @Override
    public void captureOrder(String id, PaypalCaptureDto captureDto) throws NotFoundException {
        WebClient client = WebClient.create(paypal.getSandbox());

        Donation donation = donationRepository.findById(id).orElseThrow(NotFoundException::new);

        String reply = client.post()
            .uri(paypal.captureOrder(id))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", captureDto.token())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException(clientResponse.toString())))
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException(clientResponse.toString())))
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("is error")))
            .bodyToMono(String.class)
            .block();

        donation.setStamp(LocalDateTime.now())
            .setPayed(true);

        donationRepository.save(donation);
    }
}
