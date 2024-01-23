package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimal;
import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimalId;
import at.ac.tuwien.sepr.groupphase.backend.enums.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface LikedDislikedAnimalRepository extends JpaRepository<LikedDislikedAnimal, LikedDislikedAnimalId> {

    List<LikedDislikedAnimal> findByUserIdAndLiked(UUID userId, boolean liked);

}
