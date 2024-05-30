package com.elleined.image_server_api.service.project;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
    Project save(String name) throws IOException;
    Project getById(int id);
    List<Project> getAll(Pageable pageable);

    default boolean has(Project project, Folder folder) {
        return project.getFolders().contains(folder);
    }
}
