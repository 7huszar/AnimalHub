package at.ac.tuwien.sepr.groupphase.backend.unittests.ServiceTests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestBase;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest extends TestBase {

    @Autowired
    UserService userService;

    // function - LOAD USER BY USERNAME
    // POSITIVE TEST(S)
    @Test
    public void loadUserByUserNameSuccessfully() {
        ApplicationUser givenUser = allUserEntities.get(0);

        UserDetails returnedUserDetails = userService.loadUserByUsername(givenUser.getEmail());

        assertAll(
            () -> assertEquals(givenUser.getEmail(), returnedUserDetails.getUsername()),
            () -> assertEquals(givenUser.getPassword(), returnedUserDetails.getPassword())
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void loadUserByUserNameWithEmptyUserNameThrowsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(""));
    }

    // function - FIND APPLICATION USER BY USERNAME
    // POSITIVE TEST(S)
    @Test
    public void findApplicationUserByUsernameSuccessfully() throws NotFoundException {
        ApplicationUser givenUser = allUserEntities.get(0);

        ApplicationUser returnedUser = userService.findApplicationUserByEmail(givenUser.getEmail());

        assertAll(
            () -> assertEquals(givenUser.getEmail(), returnedUser.getEmail()),
            () -> assertEquals(givenUser.getPassword(), returnedUser.getPassword())
        );
    }

    // NEGATIVE TEST(S)
    @Test
    public void findApplicationUserByUsernameWithEmptyUsernameThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.findApplicationUserByEmail(""));
    }

    // function - LOGIN
    // POSITIVE TEST(S)
    //    @Test
    //    public void loginSuccessfully() {
    //        UserLoginDto userLoginDto = UserLoginDto.builder()
    //            .email(allUserEntities.get(0).getEmail())
    //            .password(allUserEntities.get(0).getPassword())
    //            .build();
    //
    //        assertDoesNotThrow(() -> userService.login(userLoginDto));
    //    }

}