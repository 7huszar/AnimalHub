package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimal;
import at.ac.tuwien.sepr.groupphase.backend.entity.LikedDislikedAnimalId;
import at.ac.tuwien.sepr.groupphase.backend.entity.Preference;
import at.ac.tuwien.sepr.groupphase.backend.entity.PreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedDislikedAnimalRepository extends JpaRepository<LikedDislikedAnimal, LikedDislikedAnimalId> {


}
