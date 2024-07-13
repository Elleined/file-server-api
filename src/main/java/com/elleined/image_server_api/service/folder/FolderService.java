package com.elleined.image_server_api.service.folder;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FolderService extends FolderCreator {
    Folder save(Project project, String name);
    Folder getById(Project project, int id) throws ResourceNotFoundException;
    Page<Folder> getAll(Project project, Pageable pageable);

    boolean isNameAlreadyExists(String name);

    List<Folder> saveAll(Project project, List<String> names);

    default boolean has(Folder folder, ActiveImage activeImage) {
        return folder.getActiveImages().contains(activeImage);
    }

    default boolean has(Folder folder, DeletedImage deletedImage) {
        return folder.getDeletedImages().contains(deletedImage);
    }
}
