package com.elleined.image_server_api.service.image.active.db;

import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
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

    void deleteByUUID(Project project, Folder folder, ActiveImage activeImage);

    ActiveImage restore(Project project, Folder folder, DeletedImage deletedImage);

    List<ActiveImage> getAllByUUID(Project project, Folder folder, List<UUID> uuids);

    default boolean isAboveMaxFileSize(MultipartFile image) {
        return image.getSize() > MAX_FILE_SIZE;
    }

    default double getSizeInMB(MultipartFile image) {
        final double magicMultiplierToConvertBytesIntoMB = 0.00000095367432;
        double fileSizeInMB = image.getSize() * magicMultiplierToConvertBytesIntoMB;
        return Math.round(fileSizeInMB * 10000.0) / 10000.0;
    }
}
