package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact;


import java.util.UUID;

public record ContactMailDto(
    String to,
    String from,
    String name,
    String message
) {}
