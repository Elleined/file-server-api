package com.elleined.image_server_api.service.project;

import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;

import java.util.List;

public interface ProjectService {
    Project save(String name);
    Project getById(int id);
    List<Project> getAll();

    List<ActiveImage> getAllActiveImages(Project project);
    List<DeletedImage> getAllDeletedImages(Project project);

    default boolean has(Project project, ActiveImage activeImage) {
        return project.getActiveImages().contains(activeImage);
    }

    default List<Project> saveAll(List<String> names) {
        return names.stream()
                .map(this::save)
                .toList();
    }
}
