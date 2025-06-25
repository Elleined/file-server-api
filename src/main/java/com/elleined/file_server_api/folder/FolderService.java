package com.elleined.file_server_api.folder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FolderService {
    Path getUploadPath();

    // Create the folder inside the active, deleted, and failed will result to /active/folderName, /deleted/folderName, and /failed/folderName
    default void createProjectFolderDirectory(String projectName, String folderName) throws IOException {
        Path activeImagesFolderPath = this.getActiveImagesPath(projectName, folderName);
        Path deletedImagesFolderPath = this.getDeletedImagesPath(projectName, folderName);
        Path failedUploadsFolderPath = this.getFailedUploadsPath(projectName, folderName);

        if (!Files.exists(activeImagesFolderPath))
            Files.createDirectories(activeImagesFolderPath);

        if (!Files.exists(deletedImagesFolderPath))
            Files.createDirectories(deletedImagesFolderPath);

        if (!Files.exists(failedUploadsFolderPath))
            Files.createDirectories(failedUploadsFolderPath);
    }

    private Path getProjectDirectory(String projectName) {
        return Path.of(this.getUploadPath().toString()).resolve(projectName);
    }

    private Path getActiveImagesPath(String projectName) {
        return this.getProjectDirectory(projectName).resolve("active");
    }

    default Path getActiveImagesPath(String projectName, String folderName) {
        return this.getActiveImagesPath(projectName).resolve(folderName);
    }

    private Path getDeletedImagesPath(String projectName) {
        return this.getProjectDirectory(projectName).resolve("deleted");
    }

    default Path getDeletedImagesPath(String projectName, String folderName) {
        return this.getDeletedImagesPath(projectName).resolve(folderName);
    }

    private Path getFailedUploadsPath(String projectName) {
        return this.getProjectDirectory(projectName).resolve("failed");
    }

    default Path getFailedUploadsPath(String projectName, String folderName) {
        return this.getFailedUploadsPath(projectName).resolve(folderName);
    }
}
