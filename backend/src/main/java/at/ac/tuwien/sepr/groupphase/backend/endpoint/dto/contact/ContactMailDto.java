package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact;


import java.util.UUID;

public record ContactMailDto(
    UUID to,
    String from,
    String name,
    String message
) {}
