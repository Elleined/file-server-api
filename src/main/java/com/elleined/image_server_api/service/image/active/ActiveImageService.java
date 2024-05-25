package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.request.ImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public interface ActiveImageService {
    int MAX_FILE_SIZE = 1024 * 1024 * 3; // 3MB

    ActiveImage save(Project project, MultipartFile image, ImageRequest imageRequest) throws IOException;
    ActiveImage getByUUID(Project project, String uuid);
    void deleteByUUID(Project project, String uuid);
    ActiveImage restore(Project project, DeletedImage deletedImage);
    List<ActiveImage> getAllById(List<Integer> ids);

    default boolean isAboveMaxFileSize(MultipartFile image) {
        return image.getSize() > MAX_FILE_SIZE;
    }
    boolean isUUIDExists(String uuid);

    default List<ActiveImage> getAllByUUID(Project project, List<String> uuids) {
        return uuids.stream()
                .map(uuid -> getByUUID(project, uuid))
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }

    default void deleteAllByUUID(Project project, List<String> uuids) {
        uuids.forEach(uuid -> deleteByUUID(project, uuid));
    }

    default List<ActiveImage> restoreAll(Project project, List<DeletedImage> deletedImages) {
        return deletedImages.stream()
                .map(deletedImage -> restore(project, deletedImage))
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }
}
