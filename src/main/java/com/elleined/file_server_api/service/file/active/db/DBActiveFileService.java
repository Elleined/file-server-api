package com.elleined.file_server_api.service.file.active.db;

import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface DBActiveFileService {
    int MAX_FILE_SIZE = 1024 * 1024 * 3; // 3MB

    ActiveFile save(Project project,
                    Folder folder,
                    String description,
                    String additionalInformation,
                    MultipartFile file) throws IOException;

    ActiveFile getByUUID(Project project, Folder folder, UUID uuid);

    Page<ActiveFile> getAll(Project project, Folder folder, Pageable pageable);

    void deleteByUUID(Project project, Folder folder, ActiveFile activeImage);

    ActiveFile restore(Project project, Folder folder, DeletedFile deletedImage);

    default boolean isAboveMaxFileSize(MultipartFile file) {
        return file.getSize() > MAX_FILE_SIZE;
    }

    default double getSizeInMB(MultipartFile file) {
        final double magicMultiplierToConvertBytesIntoMB = 0.00000095367432;
        double fileSizeInMB = file.getSize() * magicMultiplierToConvertBytesIntoMB;
        return Math.round(fileSizeInMB * 10000.0) / 10000.0;
    }
}
