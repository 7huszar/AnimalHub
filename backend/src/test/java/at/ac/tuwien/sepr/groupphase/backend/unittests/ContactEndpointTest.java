package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.ContactEndpoint;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact.ContactMailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.mail.MailService;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ContactEndpointTest {

    @Mock
    private MailService mailService;

    @Mock
    private ShelterService shelterService;

    @InjectMocks
    private ContactEndpoint contactEndpoint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendMail_ValidContactMailDto_EmailSent() throws ValidationException {
        ContactMailDto contactMailDto = new ContactMailDto("user@example.com", "shelter@example.com", "User", "Hello");

        doNothing().when(mailService).sendShelterContactMail(any(ContactMailDto.class));

        contactEndpoint.sendMail(contactMailDto);

        verify(mailService, times(1)).sendShelterContactMail(any(ContactMailDto.class));
    }

    @Test
    public void sendMail_InvalidContactMailDto_ThrowsValidationException() throws ValidationException {
        ContactMailDto contactMailDto = new ContactMailDto("user@example.com", "shelter@example.com", "User", "Hello");

        doThrow(ValidationException.class).when(mailService).sendShelterContactMail(any(ContactMailDto.class));

        assertThrows(ValidationException.class, () -> contactEndpoint.sendMail(contactMailDto));

        verify(mailService, times(1)).sendShelterContactMail(any(ContactMailDto.class));
    }
}