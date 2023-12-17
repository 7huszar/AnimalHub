package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import org.springframework.content.commons.store.ContentStore;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface FileContentStore extends ContentStore<File, UUID> {
}
