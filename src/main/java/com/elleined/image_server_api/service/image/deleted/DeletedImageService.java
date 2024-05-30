package com.elleined.image_server_api.service.image.deleted;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.ImageService;

import java.util.List;
import java.util.UUID;

public interface DeletedImageService extends ImageService {
    List<DeletedImage> getAllByUUID(Project project, Folder folder, List<UUID> uuids);
    DeletedImage getByUUID(Project project, Folder folder, UUID uuid);

    void deleteAll(); // All images that are not accessed within 1 month will be deleted
}
