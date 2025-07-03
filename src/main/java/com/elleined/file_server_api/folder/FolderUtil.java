package com.elleined.file_server_api.folder;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.UUID;

public interface FolderUtil {
    static Path normalize(Path destinationPath, UUID folder) {
        Path normalizePath = Paths.get(folder.toString())
                .getFileName()
                .normalize();

        return destinationPath.resolve(normalizePath).normalize();
    }

    static boolean isNotInUploadPath(Path destinationPath, Path folderPath) {
        return !folderPath.startsWith(destinationPath);
    }
}
