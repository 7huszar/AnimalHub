package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

/**
 * A Dto for requesting a simple mail.
 * The purpose of the mail is depending on the context.
 */
public record MailRequestDto(
    /**
     * where to send
     */
    String email,
    /**
     * language of the mail
     */
    String lang
) {
}
