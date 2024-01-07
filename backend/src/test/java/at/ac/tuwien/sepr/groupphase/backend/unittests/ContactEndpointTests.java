package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.ContactEndpoint;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact.ContactMailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.mail.MailService;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContactEndpointTests {

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
    public void sendMailSuccessfully() throws NotFoundException {
        UUID id = UUID.randomUUID();
        ContactMailDto contactMailDto = new ContactMailDto(id, "user1@example.com", "User One", "Hello");

        Shelter shelter = new Shelter();
        // set required fields for shelter
        //when(shelterService.findById(any(UUID.class))).thenReturn(Optional.of(shelter));

        contactEndpoint.sendMail(contactMailDto);

        verify(mailService, times(1)).sendShelterContactMail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void sendMailWhenShelterNotFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        ContactMailDto contactMailDto = new ContactMailDto(id, "user1@example.com", "User One", "Hello");

        when(shelterService.findById(any(UUID.class))).thenThrow(new NotFoundException("Shelter not found"));

        assertThrows(NotFoundException.class, () -> contactEndpoint.sendMail(contactMailDto));

        verify(mailService, times(0)).sendShelterContactMail(anyString(), anyString(), anyString(), anyString());
    }
}