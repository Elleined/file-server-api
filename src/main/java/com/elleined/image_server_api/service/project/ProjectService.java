package com.elleined.image_server_api.service.project;

import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ProjectService {
    Project save(String name) throws IOException;
    Project getById(int id);
    List<Project> getAll();

    List<ActiveImage> getAllActiveImages(Project project);
    List<DeletedImage> getAllDeletedImages(Project project);

    default boolean has(Project project, ActiveImage activeImage) {
        return project.getActiveImages().contains(activeImage);
    }

    default boolean has(Project project, DeletedImage deletedImage) {
        return project.getDeletedImages().contains(deletedImage);
    }

    default List<Project> saveAll(List<String> names) throws IOException {
        List<Project> projects = new ArrayList<>();
        for (String name : names) {
            Project project = this.save(name);
            projects.add(project);
        }
        return projects;
    }
}
