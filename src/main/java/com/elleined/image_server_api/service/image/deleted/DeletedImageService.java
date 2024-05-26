package com.elleined.image_server_api.service.image.deleted;

import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.service.image.ImageService;

import java.util.List;
import java.util.UUID;

public interface DeletedImageService extends ImageService {
    List<DeletedImage> getAllByUUID(List<UUID> uuids);
    DeletedImage getByUUID(UUID uuid);

    void deleteAll(); // All images that are not accessed within 1 month will be deleted
}
