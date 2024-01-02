package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact.ContactMailDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.mail.MailService;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import java.util.UUID;


import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = ContactEndpoint.BASE_PATH)
public class ContactEndpoint {
    static final String BASE_PATH = "/api/v1/contact";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MailService mailService;
    private final ShelterService shelterService;

  public ContactEndpoint(MailService mailService, ShelterService shelterService) {
    this.mailService = mailService;
    this.shelterService = shelterService;
  }

  @Secured(UserRole.Values.USER_VALUE)
  @PostMapping("{id}")
    public void sendMail(@RequestBody ContactMailDto contactMailDto) throws NotFoundException {
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("Body of request:\n" + contactMailDto.toString());

        String to = shelterService.findById(contactMailDto.to()).getEmail();
        mailService.sendShelterContactMail("pau.joh99@gmail.com", contactMailDto.from(), contactMailDto.name(), contactMailDto.message());
    }
}

