package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user;

/**
 * A Dto for requesting a simple mail.
 * The purpose of the mail is depending on the context.
 *
 * @param email where to send the mail
 * @param lang language of the mail
 */
public record MailRequestDto(
    String email,
    String lang
) {
}
