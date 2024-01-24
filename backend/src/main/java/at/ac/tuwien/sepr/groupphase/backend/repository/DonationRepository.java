package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface DonationRepository extends JpaRepository<Donation, String> {
    Stream<Donation> findAllByShelterId(UUID id);

    @Query("SELECT payed FROM Donation WHERE id = :id")
    boolean isPayed(@Param(value = "id") String id);

    @Query("SELECT new at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.donation.DonationSumDto(a.id, a.name, SUM(d.amount))"
        + " FROM Donation d right outer join Animal a on d.receiver = a.id"
        + " WHERE a.shelter.id = :shelterId"
        + " GROUP BY a.id"
        + " ORDER BY SUM(d.amount) DESC")
    Stream<DonationSumDto> getAnimalDonations(@Param(value = "shelterId") UUID shelterId);

    @Query("SELECT SUM(amount)"
        + " FROM Donation"
        + " WHERE shelter.id = :shelterId")
    BigDecimal directShelterDonations(@Param(value = "shelterId") UUID shelterId);
}
