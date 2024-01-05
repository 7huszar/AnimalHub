package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter;

import java.util.UUID;

public record ShelterListDto(
    UUID id,
    String name,
    String address,
    String phoneNumber,
    String email,
    String donationId,
    String subscriptionId,
    Integer limit
) {
}

