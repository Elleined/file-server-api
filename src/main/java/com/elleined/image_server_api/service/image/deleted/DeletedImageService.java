package com.elleined.image_server_api.service.image.deleted;

import com.elleined.image_server_api.model.image.DeletedImage;

import java.util.List;

public interface DeletedImageService {
    List<DeletedImage> getAllById(List<Integer> ids);

    void deleteAll(); // All images that are not accessed within 1 month will be deleted
}
