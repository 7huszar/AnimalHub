package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.File;
import org.springframework.content.commons.store.ContentStore;

public interface FileContentStore extends ContentStore<File, String> {
}
