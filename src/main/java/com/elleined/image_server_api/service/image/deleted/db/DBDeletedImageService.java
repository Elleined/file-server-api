package com.elleined.image_server_api.service.image.deleted.db;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.UUID;

public interface DBDeletedImageService {
    DeletedImage getByUUID(Project project, Folder folder, UUID uuid);
    Page<DeletedImage> getAll(Project project, Folder folder, Pageable pageable);

    // All images that are not accessed within 1 month will be deleted
    void permanentlyDeleteDeletedImages() throws IOException;
}
