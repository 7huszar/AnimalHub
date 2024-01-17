package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.PaypalProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalCaptureDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalTokenDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    private final PaypalProperties paypal;
    private final DonationRepository donationRepository;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;

    private String retrieveAccessToken(String clientId, String clientSecret) {
        WebClient client = WebClient.create(paypal.getSandbox());

        AuthResponseDto reply = client.post()
            .uri(paypal.getAuth())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .header(Base64.getEncoder().encodeToString((clientId + ':' + clientSecret).getBytes()))
            .retrieve()
            .bodyToMono(AuthResponseDto.class)
            .block();

        if (reply == null || reply.accessToken() == null) {
            throw new RuntimeException("TODO what should I do");
        }

        return reply.accessToken();
    }

    @Override
    public PaypalTokenDto placeOrder(UUID id, PaypalDonationDto donationDto) throws NotFoundException {

        Shelter receiver = shelterRepository.findById(id).orElseThrow(NotFoundException::new);

        WebClient client = WebClient.create(paypal.getSandbox());

        CreateOrderDto orderDto = new CreateOrderDto(Intent.CAPTURE, new Amount("EUR", donationDto.amount()));

        PaypalPlaceOrderResponseDto reply = client.post()
            .uri(paypal.getPlaceOrder())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", this.retrieveAccessToken(paypal.getClientId(), paypal.getClientSecret()))
            .bodyValue(orderDto)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("4xx, will handle it later")))
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("5xx, will handle it later")))
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("is error")))
            .bodyToMono(PaypalPlaceOrderResponseDto.class)
            .block();

        if (reply == null || reply.links() == null || reply.links().isEmpty()) {
            throw new RuntimeException("oh man");
        }

        Donation donation = Donation.builder()
            .user(null)
            .stamp(null)
            .payed(false)
            .id(reply.id())
            .shelter(receiver)
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

        return new PaypalTokenDto(linkForPayer.href());
    }

    @Override
    public void captureOrder(String id, PaypalCaptureDto captureDto) throws NotFoundException {
        WebClient client = WebClient.create(paypal.getSandbox());

        Donation donation = donationRepository.findById(id).orElseThrow(NotFoundException::new);
        ApplicationUser user = userRepository.findById(captureDto.user()).orElseThrow(NotFoundException::new);

        String reply = client.post()
            .uri(paypal.captureOrder(id))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", captureDto.token())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("4xx, will handle it later")))
            .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("5xx, will handle it later")))
            .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("is error")))
            .bodyToMono(String.class)
            .block();

        donation.setUser(user)
            .setStamp(LocalDateTime.now())
            .setPayed(true);

        donationRepository.save(donation);
    }
}
