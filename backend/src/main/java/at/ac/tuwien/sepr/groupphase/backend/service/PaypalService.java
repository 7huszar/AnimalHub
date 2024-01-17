package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalCaptureDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalDonationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.paypal.PaypalTokenDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.UUID;

public interface PaypalService {

    PaypalTokenDto placeOrder(UUID id, PaypalDonationDto donationDto) throws NotFoundException;

    void captureOrder(String id, PaypalCaptureDto captureDto) throws NotFoundException;
}
