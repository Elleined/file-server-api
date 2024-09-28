package com.elleined.file_server_api.service.folder;

import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FolderCreator {
    Path getUploadPath();
    default void createFolder() throws IOException {
        if (!Files.exists(this.getUploadPath()))
            Files.createDirectories(this.getUploadPath());
    }

    // Create the default directories for a project active, deleted, and failed
    default void createFolder(Project project) throws IOException {
        Path projectDirectory = this.getProjectDirectory(project);
        Path activeImagesPath = this.getActiveImagesPath(project);
        Path deletedImagesPath = this.getDeletedImagesPath(project);
        Path failedUploadsPath = this.getFailedUploadsPath(project);

        if (!Files.exists(projectDirectory))
            Files.createDirectories(projectDirectory);

        if (!Files.exists(activeImagesPath))
            Files.createDirectories(activeImagesPath);

        if (!Files.exists(deletedImagesPath))
            Files.createDirectories(deletedImagesPath);

        if (!Files.exists(failedUploadsPath))
            Files.createDirectories(failedUploadsPath);

        for (Folder folder : project.getFolders()) {
            this.createFolder(project, folder);
        }
    }

    // Create the folders in the project active, deleted, and failed directories
    default void createFolder(Project project, Folder folder) throws IOException {
        Path activeImagesFolderPath = this.getActiveImagesPath(project, folder);
        Path deletedImagesFolderPath = this.getDeletedImagesPath(project, folder);
        Path failedUploadsFolderPath = this.getFailedUploadsPath(project, folder);

        if (!Files.exists(activeImagesFolderPath))
            Files.createDirectories(activeImagesFolderPath);

        if (!Files.exists(deletedImagesFolderPath))
            Files.createDirectories(deletedImagesFolderPath);

        if (!Files.exists(failedUploadsFolderPath))
            Files.createDirectories(failedUploadsFolderPath);
    }

    private Path getProjectDirectory(Project project) {
        return Path.of(this.getUploadPath().toString(), project.getName());
    }

    private Path getActiveImagesPath(Project project) {
        return Path.of(this.getProjectDirectory(project).toString(), "active");
    }

    default Path getActiveImagesPath(Project project, Folder folder) {
        return Path.of(this.getActiveImagesPath(project).toString(), folder.getName());
    }

    private Path getDeletedImagesPath(Project project) {
        return Path.of(this.getProjectDirectory(project).toString(), "deleted");
    }

    default Path getDeletedImagesPath(Project project, Folder folder) {
        return Path.of(this.getDeletedImagesPath(project).toString(), folder.getName());
    }

    private Path getFailedUploadsPath(Project project) {
        return Path.of(this.getProjectDirectory(project).toString(), "failed");
    }

    default Path getFailedUploadsPath(Project project, Folder folder) {
        return Path.of(this.getFailedUploadsPath(project).toString(), folder.getName());
    }
}
