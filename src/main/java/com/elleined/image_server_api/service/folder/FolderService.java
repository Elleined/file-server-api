package com.elleined.image_server_api.service.folder;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FolderService extends FolderCreator {
    Folder save(Project project, String name);
    Folder getById(Project project, int id) throws ResourceNotFoundException;
    List<Folder> getAll(Project project, Pageable pageable);
    List<Folder> getAllById(Project project, List<Integer> ids);

    boolean isNameAlreadyExists(String name);

    List<ActiveImage> getAllActiveImages(Project project, Folder folder, Pageable pageable);
    List<DeletedImage> getAllDeletedImages(Project project, Folder folder, Pageable pageable);

    List<Folder> saveAll(Project project, List<String> names);

    default boolean has(Folder folder, ActiveImage activeImage) {
        return folder.getActiveImages().contains(activeImage);
    }

    default boolean has(Folder folder, DeletedImage deletedImage) {
        return folder.getDeletedImages().contains(deletedImage);
    }
}
