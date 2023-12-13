package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {

    ApplicationUser findByEmail(String email);

    List<ApplicationUser> findApplicationUsersByRole(UserRole role);

    ApplicationUser findApplicationUsersByRoleAndIdEquals(UserRole role, UUID id);

    boolean existsByEmail(String email);

    List<ApplicationUser> findUsersByEmailIgnoreCaseContaining(String email);

}
