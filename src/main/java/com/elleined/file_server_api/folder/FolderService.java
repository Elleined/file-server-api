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

    default boolean hasInvalidLength(String folder) {
        return folder.length() > 25;
    }

    default boolean hasInvalidCharacters(String folder) {
        return folder.contains(".") ||
                folder.contains("/") ||
                folder.contains("\\") ||
                folder.contains("%");
    }

    default boolean isSymbolicLink(Path folder) {
        return Files.isSymbolicLink(folder);
    }

    default Path normalize(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path normalizePath = Paths.get(folder.strip())
                .getFileName()
                .normalize();

        return uploadPath.resolve(normalizePath).normalize();
    }

    default boolean isInUploadPath(Path folderPath) throws IOException {
        Path uploadPath = this.getUploadPath();
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
