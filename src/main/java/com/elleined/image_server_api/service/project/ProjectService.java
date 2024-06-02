package com.elleined.image_server_api.service.project;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ProjectService {
    int MAX_STORAGE_SIZE_IN_MB = 1024 * 1024 * 300; // 300MB

    Project save(String name) throws IOException;
    Project getById(int id);
    List<Project> getAll(Pageable pageable);

    default boolean has(Project project, Folder folder) {
        return project.getFolders().contains(folder);
    }

    default boolean isStorageMax(Project project) {
        return project.getFolders().stream()
                .map(Folder::getActiveImages)
                .flatMap(Collection::stream)
                .map(ActiveImage::getFileSizeInMB)
                .reduce(0D, Double::sum) >= MAX_STORAGE_SIZE_IN_MB;
    }
}
