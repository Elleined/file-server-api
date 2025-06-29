package com.elleined.file_server_api.folder;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public interface FolderValidator {
    static Path normalize(Path destinationPath, UUID folder) {
        Path normalizePath = Paths.get(folder.toString())
                .getFileName()
                .normalize();

        return destinationPath.resolve(normalizePath).normalize();
    }

    static boolean isNotInUploadPath(Path destinationPath, Path folderPath) {
        return !folderPath.startsWith(destinationPath);
    }

    static boolean exists(Path folderPath) {
        return Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS);
    }

    static boolean isSymbolicLink(Path folderPath) {
        return Files.isSymbolicLink(folderPath);
    }
}
