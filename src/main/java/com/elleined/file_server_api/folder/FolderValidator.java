package com.elleined.file_server_api.folder;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FolderValidator {
    static Path normalize(Path uploadPath, String folder) {
        Path normalizePath = Paths.get(folder.strip())
                .getFileName()
                .normalize();

        return uploadPath.resolve(normalizePath).normalize();
    }

    static boolean isNotInUploadPath(Path uploadPath, Path folderPath) {
        return !folderPath.startsWith(uploadPath);
    }

    static boolean exists(Path folderPath) {
        return Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS);
    }

    static boolean isSymbolicLink(Path folderPath) {
        return Files.isSymbolicLink(folderPath);
    }

    static boolean isAllowed(Path folderPath) {
        return Files.isWritable(folderPath) &&
                Files.isReadable(folderPath);
    }
}
