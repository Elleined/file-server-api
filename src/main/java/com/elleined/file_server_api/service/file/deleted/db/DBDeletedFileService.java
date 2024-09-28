package com.elleined.file_server_api.service.file.deleted.db;

import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.UUID;

public interface DBDeletedFileService {
    DeletedFile getByUUID(Project project, Folder folder, UUID uuid);
    Page<DeletedFile> getAll(Project project, Folder folder, Pageable pageable);

    // All images that are not accessed within 1 month will be deleted
    void permanentlyDeleteDeletedImages() throws IOException;
}
