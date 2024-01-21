package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface DonationRepository extends JpaRepository<Donation, String> {
    Stream<Donation> findAllByShelterId(UUID id);

    @Query("SELECT payed FROM Donation WHERE id = :id")
    boolean isPayed(@Param(value = "id") String id);
}
