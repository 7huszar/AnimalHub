package at.ac.tuwien.sepr.groupphase.backend.service.conflict;

import at.ac.tuwien.sepr.groupphase.backend.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@RequiredArgsConstructor
@Component
public class ShelterConflict {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShelterRepository shelterRepository;


}
