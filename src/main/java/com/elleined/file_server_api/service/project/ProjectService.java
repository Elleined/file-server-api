package com.elleined.file_server_api.service.project;

import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ProjectService {
    int MAX_STORAGE_SIZE_IN_MB = 1024 * 1024 * 300; // 300MB

    Project save(String name);

    Project getById(int id);
    Project getByName(String name);
    Page<Project> getAll(Pageable pageable);
    List<Project> getAll();

    boolean isNameAlreadyExists(String name);

    default boolean has(Project project, Folder folder) {
        return project.getFolders().contains(folder);
    }

    default boolean isStorageMax(Project project, double fileSizeInMB) {
        return project.getFolders().stream()
                .map(Folder::getActiveImages)
                .flatMap(Collection::stream)
                .map(ActiveFile::getFileSizeInMB)
                .reduce(0D, Double::sum) + fileSizeInMB >= MAX_STORAGE_SIZE_IN_MB;
    }
}
