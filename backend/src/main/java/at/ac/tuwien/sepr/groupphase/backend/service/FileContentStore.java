package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import org.springframework.content.commons.store.ContentStore;

import java.util.UUID;

public interface FileContentStore extends ContentStore<File, UUID> {
}
