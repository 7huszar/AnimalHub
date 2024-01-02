package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.contact.ContactMailDto;
import at.ac.tuwien.sepr.groupphase.backend.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;


import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = ContactEndpoint.BASE_PATH)
public class ContactEndpoint {
    static final String BASE_PATH = "/api/v1/contact";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MailService mailService;

  public ContactEndpoint(MailService mailService) {
    this.mailService = mailService;
  }

  @Secured(UserRole.Values.USER_VALUE)
  @PostMapping
    public void sendMail(@RequestBody ContactMailDto contactMailDto) {
        LOGGER.info("POST " + BASE_PATH);
        LOGGER.debug("Body of request:\n" + contactMailDto.toString());
        mailService.sendShelterContactMail(contactMailDto.to(), contactMailDto.from(), contactMailDto.name(), contactMailDto.message());
    }
}

