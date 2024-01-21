package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact;


public record ContactMailDto(
    String to,
    String from,
    String name,
    String message
) {
}
