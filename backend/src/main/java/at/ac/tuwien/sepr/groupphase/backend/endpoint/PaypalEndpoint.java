package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalCaptureDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.PaypalException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.PaypalService;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalLink;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

@RestController
@RequestMapping(value = PaypalEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class PaypalEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static final String BASE_PATH = "api/v1/paypal";
    private final PaypalService paypalService;

    /**
     * Places an order for update.
     *
     * @param id UUID of shelter
     * @param donationDto Dto - contains amount
     * @return Dto containing link for confirming PayPal payment
     */
    @ResponseStatus(HttpStatus.OK)
    @Secured(UserRole.Values.USER_VALUE)
    @PostMapping("/donate/{id}")
    public PaypalLink placeOrder(@PathVariable UUID id, @RequestBody PaypalDonationDto donationDto) throws NotFoundException, ValidationException, ConflictException, PaypalException {
        LOGGER.info("POST " + BASE_PATH + "/donate/{}", id);
        LOGGER.debug("placeOrder({}, {})", id, donationDto);

        try {
            return this.paypalService.placeOrder(id, donationDto);
        } catch (ValidationException | ConflictException e) {
            LOGGER.warn(e.getMessage());
            throw e;
        } catch (PaypalException e) {
            LOGGER.warn(e.getResponse().toString());
            throw e;
        }


    }

    /**
     * Captures (=authorizes) and finishes the order.
     *
     * @param id PayPal id of transaction
     * @throws NotFoundException if user or transaction not found
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured(UserRole.Values.USER_VALUE)
    @PostMapping("/capture/{id}")
    public void captureOrder(@PathVariable String id) throws NotFoundException, ValidationException, ConflictException, PaypalException {
        LOGGER.info("POST " + BASE_PATH + "/capture/{}", id);
        LOGGER.debug("captureDto({})", id);

        try {
            this.paypalService.captureOrder(id);
        } catch (ValidationException | ConflictException e) {
            LOGGER.warn(e.getMessage());
            throw e;
        } catch (PaypalException e) {
            LOGGER.warn(e.getResponse().toString());
            throw e;
        }
    }
}
