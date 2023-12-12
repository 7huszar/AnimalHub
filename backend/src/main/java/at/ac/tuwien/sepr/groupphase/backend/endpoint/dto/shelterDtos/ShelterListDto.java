package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelterDtos;

import java.util.UUID;

public record ShelterListDto(
    UUID id,
    String name,
    String address,
    String phoneNumber,
    String email,
    Integer limit
) {
}

