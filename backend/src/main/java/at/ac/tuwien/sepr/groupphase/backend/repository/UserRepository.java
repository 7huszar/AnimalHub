package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {

    ApplicationUser findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("update ApplicationUser u set u.status = :status where u.email = :email")
    void updateStatus(@Param(value = "email") String email, @Param(value = "status") UserStatus status);
}
