package com.elleined.image_server_api.service.image;

import com.elleined.image_server_api.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public interface ImageService {
    String uploadDirectory = "/pictures";

    String save(Project project, MultipartFile image) throws IOException;
    byte[] getImage(Project project, String fileName) throws IOException;
    void transfer(Project project, MultipartFile multipartFile) throws IOException;

    default String getUniqueFileName(MultipartFile image) {
        String currentDateAndTime = LocalDateTime.now().toString();
        String fileName = image.getOriginalFilename();
        return STR."\{currentDateAndTime}_\{fileName}";
    }

    default void createFolders(Project project) throws IOException {
        Path projectDirectory = Path.of(STR."\{uploadDirectory}/\{project.getName()}");
        Path activeImagesDirectory = Path.of(this.getActiveImagesPath(project));
        Path deletedImagesDirectory = Path.of(this.getDeletedImagesPath(project));

        if (!Files.exists(projectDirectory)) Files.createDirectories(projectDirectory);
        if (!Files.exists(activeImagesDirectory)) Files.createDirectories(activeImagesDirectory);
        if (!Files.exists(deletedImagesDirectory)) Files.createDirectories(deletedImagesDirectory);
    }

    default String getActiveImagesPath(Project project) {
        return STR."\{uploadDirectory}/\{project.getName()}/active";
    }
    default String getDeletedImagesPath(Project project) {
        return STR."\{uploadDirectory}/\{project.getName()}/deleted";
    }
}

