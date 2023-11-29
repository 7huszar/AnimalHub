package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

//TODO: replace this class with a correct ApplicationUser JPARepository implementation
@Repository
public class UserRepository {

    private final ApplicationUser externalUser;
    private final ApplicationUser shelterUser;
    private final ApplicationUser admin;

    @Autowired
    public UserRepository(PasswordEncoder passwordEncoder) {
        externalUser = new ApplicationUser("external_user@email.com", passwordEncoder.encode("password"), UserRole.EXTERNAL_USER);
        admin = new ApplicationUser("admin@email.com", passwordEncoder.encode("password"), UserRole.ADMIN);
        shelterUser = new ApplicationUser("shelter_user@email.com", passwordEncoder.encode("password"), UserRole.SHELTER_USER);
    }

    public ApplicationUser findUserByEmail(String email) {
        if (email.equals(externalUser.getEmail())) {
            return externalUser;
        }
        if (email.equals(admin.getEmail())) {
            return admin;
        }
        if(email.equals(shelterUser.getEmail())){
            return shelterUser;
        }
        return null; // In this case null is returned to fake Repository behavior
    }


}
