package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.enums.Gender;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {

    @Query("SELECT a FROM Animal a "
        + "WHERE (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
        + "AND (:gender IS NULL OR a.gender = :gender) "
        + "AND (:species IS NULL OR a.species = :species) "
        + "AND (:race IS NULL OR LOWER(a.germanRace) LIKE LOWER(CONCAT('%', :race, '%')) OR LOWER(a.englishRace) LIKE LOWER(CONCAT('%', :race, '%'))) "
        + "AND (:bornBefore IS NULL OR a.birthDate <= :bornBefore) "
        + "AND (:bornAfter IS NULL OR a.birthDate >= :bornAfter) "
        + "AND (:heavierThan = 0 OR a.weight > :heavierThan) "
        + "AND (:lighterThan = 0 OR a.weight < :lighterThan) "
        + "AND (:tallerThan = 0 OR a.height > :tallerThan) "
        + "AND (:smallerThan = 0 OR a.height < :smallerThan)")
    List<Animal> findAnimalsByParameters(
        @Param("name") String name,
        @Param("gender") Gender gender,
        @Param("species") Species species,
        @Param("race") String race,
        @Param("bornBefore") LocalDate bornBefore,
        @Param("bornAfter") LocalDate bornAfter,
        @Param("heavierThan") Double heavierThan,
        @Param("lighterThan") Double lighterThan,
        @Param("tallerThan") Double tallerThan,
        @Param("smallerThan") Double smallerThan
    );


}
