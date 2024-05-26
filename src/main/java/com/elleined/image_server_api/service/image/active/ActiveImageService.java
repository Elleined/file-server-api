package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public interface ActiveImageService {
    int MAX_FILE_SIZE = 1024 * 1024 * 2; // 2MB

    ActiveImage save(Project project,
                     String description,
                     String additionalInformation,
                     MultipartFile image) throws IOException;

    ActiveImage getByUUID(Project project, UUID uuid);

    void deleteByUUID(Project project, UUID uuid);

    ActiveImage restore(Project project, DeletedImage deletedImage);

    List<ActiveImage> getAllByUUID(List<UUID> uuids);

    default boolean isAboveMaxFileSize(MultipartFile image) {
        return image.getSize() > MAX_FILE_SIZE;
    }

    default List<ActiveImage> getAllByUUID(Project project, List<UUID> uuids) {
        return uuids.stream()
                .map(uuid -> getByUUID(project, uuid))
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();
    }

    default void deleteAllByUUID(Project project, List<UUID> uuids) {
        uuids.forEach(uuid -> deleteByUUID(project, uuid));
    }

    default List<ActiveImage> restoreAll(Project project, List<DeletedImage> deletedImages) {
        return deletedImages.stream()
                .map(deletedImage -> restore(project, deletedImage))
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();
    }
}
