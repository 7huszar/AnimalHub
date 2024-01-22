package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Appointment;
import at.ac.tuwien.sepr.groupphase.backend.enums.AppointmentStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByUserId(@NonNull UUID userId);

    List<Appointment> findByShelterIdAndStatusNot(@NonNull UUID shelterId, AppointmentStatus status);
}
