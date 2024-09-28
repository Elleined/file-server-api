package com.elleined.file_server_api.service.folder;

import com.elleined.file_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FolderService extends FolderCreator {
    Folder save(Project project, String name);
    Folder getById(Project project, int id) throws ResourceNotFoundException;
    Page<Folder> getAll(Project project, Pageable pageable);

    boolean isNameAlreadyExists(String name);

    List<Folder> saveAll(Project project, List<String> names);

    default boolean has(Folder folder, ActiveFile activeImage) {
        return folder.getActiveImages().contains(activeImage);
    }

    default boolean has(Folder folder, DeletedFile deletedImage) {
        return folder.getDeletedImages().contains(deletedImage);
    }
}
