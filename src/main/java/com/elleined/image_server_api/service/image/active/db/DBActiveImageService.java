package com.elleined.image_server_api.service.image.active.db;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DBActiveImageService {
    int MAX_FILE_SIZE = 1024 * 1024 * 3; // 3MB

    ActiveImage save(Project project,
                     Folder folder,
                     String description,
                     String additionalInformation,
                     MultipartFile image) throws IOException;

    ActiveImage getByUUID(Project project, Folder folder, UUID uuid);

    Page<ActiveImage> getAll(Project project, Folder folder, Pageable pageable);

    void deleteByUUID(Project project, Folder folder, ActiveImage activeImage);

    ActiveImage restore(Project project, Folder folder, DeletedImage deletedImage);

    default boolean isAboveMaxFileSize(MultipartFile image) {
        return image.getSize() > MAX_FILE_SIZE;
    }

    default double getSizeInMB(MultipartFile image) {
        final double magicMultiplierToConvertBytesIntoMB = 0.00000095367432;
        double fileSizeInMB = image.getSize() * magicMultiplierToConvertBytesIntoMB;
        return Math.round(fileSizeInMB * 10000.0) / 10000.0;
    }
}
