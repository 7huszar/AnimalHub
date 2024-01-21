package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.shelter.ShelterDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {

    ApplicationUser findByEmail(String email);

    boolean existsByEmail(String email);

    List<ApplicationUser> findApplicationUsersByRole(UserRole role);

    ApplicationUser findApplicationUsersByRoleAndIdEquals(UserRole role, UUID id);

    List<ApplicationUser> findUsersByEmailIgnoreCaseContaining(String email);

    @Modifying
    @Query("update ApplicationUser u set u.role = :role where u.email = :email")
    void updateRole(@Param(value = "email") String email, @Param(value = "role") UserRole status);

    @Modifying
    @Query("update ApplicationUser u set u.password = :password where u.email = :email")
    void updatePassword(@Param(value = "email") String email, @Param(value = "password") String password);

    @Query("SELECT 1 FROM ApplicationUser WHERE id = :id")
    boolean isMember(@Param(value = "id") UUID id);

    ApplicationUser findApplicationUserById(UUID id);

    ApplicationUser findApplicationUserByShelter_Id(UUID shelterId);

}
