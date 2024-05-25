package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.request.ImageRequest;
import com.elleined.image_server_api.service.CustomService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

public interface ActiveImageService extends CustomService<ActiveImage> {
    int MAX_FILE_SIZE = 1024 * 1024 * 3; // 3MB

    ActiveImage save(ImageRequest imageRequest);
    ActiveImage getByUUID(String uuid);
    void deleteByUUID(String uuid);
    ActiveImage restoreByUUID(String uuid);

    default boolean isAboveMaxFileSize(MultipartFile image) {
        return image.getSize() > MAX_FILE_SIZE;
    }

    default List<ActiveImage> saveAll(List<ImageRequest> imageRequests) {
        return imageRequests.stream()
                .map(this::save)
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }

    default List<ActiveImage> getAllByUUID(List<String> uuids) {
        return uuids.stream()
                .map(this::getByUUID)
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }

    default void deleteAllByUUID(List<String> uuids) {
        uuids.forEach(this::deleteByUUID);
    }

    default List<ActiveImage> restoreAllByUUID(List<String> uuids) {
        return uuids.stream()
                .map(this::restoreByUUID)
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();
    }
}
