package at.ac.tuwien.sepr.groupphase.backend.repository.files;


import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(path = "files", collectionResourceRel = "files")
public interface FileRepository extends JpaRepository<File, UUID> {

}
