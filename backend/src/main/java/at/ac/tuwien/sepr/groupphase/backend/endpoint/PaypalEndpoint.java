package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalCaptureDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalTokenDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = PaypalEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class PaypalEndpoint {
    static final String BASE_PATH = "api/v1/paypal";
    private final PaypalService paypalService;

    @ResponseStatus(HttpStatus.OK)
    @Secured(UserRole.Values.USER_VALUE)
    @PostMapping("/donate/{id}")
    public PaypalTokenDto placeOrder(@PathVariable UUID id, @RequestBody PaypalDonationDto donationDto) {
        return this.paypalService.placeOrder(donationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured(UserRole.Values.USER_VALUE)
    @PostMapping("/capture")
    public void captureOrder(PaypalCaptureDto captureDto) {
        this.paypalService.captureOrder(captureDto);
    }
}
