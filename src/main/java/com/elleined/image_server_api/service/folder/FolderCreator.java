package com.elleined.image_server_api.service.folder;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.project.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface FolderCreator {

    default void createFolders(Project project, List<Folder> folders) throws IOException {
        Path uploadDirectory = this.getUploadDirectory();
        Path projectDirectory = this.getProjectDirectory(project);
        Path activeImagesPath = this.getActiveImagesPath(project);
        Path deletedImagesPath = this.getDeletedImagesPath(project);
        Path failedUploadsPath = this.getFailedUploadsPath(project);

        List<Path> activeImagesFolderPath = folders.stream()
                .map(folder -> this.getActiveImagesPath(project, folder))
                .toList();

        List<Path> getDeletedImagesFolderPath = folders.stream()
                .map(folder -> this.getDeletedImagesPath(project, folder))
                .toList();

        List<Path> getFailedUploadsFolderPath = folders.stream()
                .map(folder -> this.getDeletedImagesPath(project, folder))
                .toList();

        if (!Files.exists(uploadDirectory))
            Files.createDirectories(uploadDirectory);

        if (!Files.exists(projectDirectory))
            Files.createDirectories(projectDirectory);

        if (!Files.exists(activeImagesPath))
            Files.createDirectories(activeImagesPath);

        if (!Files.exists(deletedImagesPath))
            Files.createDirectories(deletedImagesPath);

        if (!Files.exists(failedUploadsPath))
            Files.createDirectories(failedUploadsPath);

        if (!Files.exists())
            Files.createDirectories(pictureDirectory);

        if (!Files.exists())
            Files.createDirectories(pictureDirectory);

        if (!Files.exists())
            Files.createDirectories(pictureDirectory);
    }

    private Path getUploadDirectory() {
        return Path.of("src/main/resources/pictures");
    }

    private Path getProjectDirectory(Project project) {
        return Path.of(this.getUploadDirectory().toString(), project.getName());
    }

    private Path getActiveImagesPath(Project project) {
        return Path.of(this.getProjectDirectory(project).toString(), "active");
    }

    private Path getDeletedImagesPath(Project project) {
        return Path.of(this.getProjectDirectory(project).toString(), "deleted");
    }

    private Path getFailedUploadsPath(Project project) {
        return Path.of(this.getProjectDirectory(project).toString(), "failed");
    }

    default Path getActiveImagesPath(Project project, Folder folder) {
        return Path.of(this.getActiveImagesPath(project).toString(), folder.getName());
    }

    default Path getDeletedImagesPath(Project project, Folder folder) {
        return Path.of(this.getDeletedImagesPath(project).toString(), folder.getName());
    }

    default Path getFailedUploadsPath(Project project, Folder folder) {
        return Path.of(this.getFailedUploadsPath(project).toString(), folder.getName());
    }
}
