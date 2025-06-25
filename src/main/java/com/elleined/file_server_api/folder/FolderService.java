package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
import jakarta.validation.constraints.NotBlank;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FolderService {
    void save(@NotBlank String folder);
    void delete(@NotBlank String folder);

    Path getUploadPath();

    // General purpose folder name sanitation to prevent traversal attacks
    default Path sanitize(String folder) {
        Path sanitize = Paths.get(folder.strip())
                .normalize()
                .toAbsolutePath()
                .normalize();

        if (Files.isSymbolicLink(sanitize))
            throw new FileServerAPIException("Symbolic links are not allowed");

        return sanitize.resolve(folder);
    }
}
