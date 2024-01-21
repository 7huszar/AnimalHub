package at.ac.tuwien.sepr.groupphase.backend.service.validator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class PaypalValidator {
    public void validateDonationOrder(UUID id, PaypalDonationDto donationDto) throws ValidationException {
        List<String> validationErrors = new ArrayList<>();
        if (id == null) {
            validationErrors.add("no shelter id given");
        }

        if (donationDto.donator() == null) {
            validationErrors.add("no donator id given");
        }

        if (donationDto.amount() == null) {
            validationErrors.add("no amount given");
        } else {
            try {
                new BigDecimal(donationDto.amount());
            } catch (NumberFormatException e) {
                validationErrors.add(e.getMessage());
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("failed to validate data for placing order", validationErrors);
        }

    }

    public void validateCaptureOrder(String id) throws ValidationException {
        if (id == null || id.isBlank()) {
            throw new ValidationException("failed to validate data for capturing order",
                Collections.singletonList("no Donation id given"));
        }
    }
}
