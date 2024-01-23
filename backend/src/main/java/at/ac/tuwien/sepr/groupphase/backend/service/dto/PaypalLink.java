package at.ac.tuwien.sepr.groupphase.backend.service.dto;

public record PaypalLink(
    String href,
    String rel,
    String method
) {}
