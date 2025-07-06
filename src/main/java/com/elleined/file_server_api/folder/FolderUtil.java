package com.elleined.file_server_api.folder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public interface FolderUtil {
    Path getUploadPath() throws IOException;

    default boolean isInUploadPath(Path folderPath) throws IOException {
        return folderPath.startsWith(getUploadPath());
    }

    default boolean isSymbolicLink(Path folderPath) {
        return Files.isSymbolicLink(folderPath);
    }

    default boolean exists(Path folderPath) {
        return Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS);
    }
}
