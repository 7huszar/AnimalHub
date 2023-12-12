package at.ac.tuwien.sepr.groupphase.backend.endpoint;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import at.ac.tuwien.sepr.groupphase.backend.service.ShelterService;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = UserEndpoint.BASE_PATH)
@RequiredArgsConstructor
public class UserEndpoint {

    static final String BASE_PATH = "/api/v1/users";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    @Secured(UserRole.Values.ADMIN_VALUE)
    @GetMapping("/search")
    @Operation(summary = "Get list of filtered users", security = @SecurityRequirement(name = "apiKey"))
    public List<UserDetailDto> searchShelters(UserSearchDto searchParameters) {
        LOGGER.info("GET " + BASE_PATH);
        LOGGER.debug("request parameters: {}", searchParameters);

        return userService.search(searchParameters);
    }

}
