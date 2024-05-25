package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.request.ImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public interface ActiveImageService {
    int MAX_FILE_SIZE = 1024 * 1024 * 3; // 3MB

    ActiveImage save(MultipartFile image, ImageRequest imageRequest) throws IOException;
    ActiveImage getByUUID(String uuid);
    void deleteByUUID(String uuid);
    ActiveImage restore(DeletedImage deletedImage);
    List<ActiveImage> getAllById(List<Integer> ids);

    default boolean isAboveMaxFileSize(MultipartFile image) {
        return image.getSize() > MAX_FILE_SIZE;
    }
    boolean isUUIDExists(String uuid);

    default List<ActiveImage> getAllByUUID(List<String> uuids) {
        return uuids.stream()
                .map(this::getByUUID)
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }

    default void deleteAllByUUID(List<String> uuids) {
        uuids.forEach(this::deleteByUUID);
    }

    default List<ActiveImage> restoreAll(List<DeletedImage> deletedImages) {
        return deletedImages.stream()
                .map(this::restore)
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }
}
