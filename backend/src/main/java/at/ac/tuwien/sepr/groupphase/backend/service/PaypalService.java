package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.PaypalException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.dto.PaypalLink;

import java.util.UUID;

public interface PaypalService {

    /**
     * Places an order for a PayPal payment by using said api.
     * A link is returned where the payer has to confirm the order and this service has to
     * capture order afterward, before payment is complete.
     *
     * @param id Receiver of payment
     * @param donationDto Dto containing the donator and amount of payment
     * @return PaypalLinkDto containing a href to PayPal service where payer has to confirm payment
     * @throws NotFoundException If either donator or receiver not found
     * @throws ValidationException If failed to validate data
     * @throws ConflictException If donator and receiver are the same
     */
    PaypalLink placeOrder(UUID id, PaypalDonationDto donationDto) throws NotFoundException, ValidationException, ConflictException, PaypalException, NotAuthorizedException;

    /**
     * Capture PayPal order and thus finalizes payment process.
     * The order has to be placed and confirmed before capturing it.
     *
     * @param id Identifier of Donation in repository
     * @throws NotFoundException if Donation not found
     * @throws ValidationException if id not given
     * @throws ConflictException if order already paid
     */
    void captureOrder(String id) throws NotFoundException, ValidationException, ConflictException, PaypalException;
}
