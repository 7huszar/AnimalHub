package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.PaypalProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalCaptureDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalTokenDto;
import at.ac.tuwien.sepr.groupphase.backend.service.PaypalService;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.Amount;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.AuthResponseDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.CreateOrderDto;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.Intent;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalPlaceOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    private final PaypalProperties paypal;
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
    public PaypalTokenDto placeOrder(PaypalDonationDto donationDto) {
        WebClient client = WebClient.create(paypal.getSandbox());

        CreateOrderDto orderDto = new CreateOrderDto(Intent.CAPTURE, new Amount("EUR", donationDto.amount()));

        PaypalPlaceOrderResponseDto reply = client.post()
            .uri(paypal.getPlaceOrder())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", this.retrieveAccessToken(paypal.getClientId(), paypal.getClientSecret()))
            .bodyValue(orderDto)
            .retrieve()
            .bodyToMono(PaypalPlaceOrderResponseDto.class)
            .block();

        if (reply == null || reply.links() == null || reply.links().isEmpty()) {
            throw new RuntimeException("oh man");
        }
        return new PaypalTokenDto(reply.links().get(0).href());
    }

    @Override
    public void captureOrder(PaypalCaptureDto captureDto) {
        WebClient client = WebClient.create(paypal.getSandbox());

        String reply = client.post()
            .uri(paypal.captureOrder(captureDto.id()))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", captureDto.token())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
