package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.animal.AnimalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Animal;
import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimal;
import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimalId;
import at.ac.tuwien.sepr.groupphase.backend.entity.Preference;
import at.ac.tuwien.sepr.groupphase.backend.entity.PreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface LikedDislikedAnimalRepository extends JpaRepository<LikedDislikedAnimal, LikedDislikedAnimalId> {

    List<LikedDislikedAnimal> findByUserIdAndLike(UUID user_id, boolean like);

}
