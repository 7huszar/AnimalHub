package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Shelter;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, UUID> {

    List<Shelter> findByNameContainsIgnoreCaseAndAddressContainsIgnoreCaseAndPhoneNumberContainsIgnoreCaseAndEmailContainsIgnoreCase(String name, String address, String phoneNumber, String email);

    boolean existsById(@NonNull UUID uuid);
}

