package com.elleined.file_server_api.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FolderService {
    void create(@Size(max = 25) @NotBlank String folder) throws IOException;
    void remove(@NotBlank String folder) throws IOException;
    Path get(@NotBlank String folder) throws IOException;
    Path getUploadPath() throws IOException;

    default boolean isNullOrBlank(String folder) {
        return folder == null || folder.isBlank();
    }

    default boolean hasInvalidLength(String folder, int maxLength) {
        return folder.length() > maxLength;
    }

    default boolean isAlphaNumeric(String folder) {
        return folder.matches("^[a-zA-Z0-9]+$");
    }

    default Path normalize(Path uploadPath, String folder) {
        Path normalizePath = Paths.get(folder.strip())
                .getFileName()
                .normalize();

        return uploadPath.resolve(normalizePath).normalize();
    }

    default boolean isSymbolicLink(Path folderPath) {
        return Files.isSymbolicLink(folderPath);
    }

    default boolean isInUploadPath(Path uploadPath, Path folderPath) {
        return folderPath.startsWith(uploadPath);
    }

    default boolean exists(Path folderPath) {
        return Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS);
    }

    default boolean isAllowed(Path folderPath) {
        return Files.isWritable(folderPath) &&
                Files.isReadable(folderPath);
    }
}
