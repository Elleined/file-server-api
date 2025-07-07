package com.elleined.file_server_api.folder;

import java.io.IOException;
import java.nio.file.Path;

public interface FolderUtil {
    Path getUploadPath() throws IOException;

    default boolean isInUploadPath(Path folderPath) throws IOException {
        return folderPath.startsWith(getUploadPath());
    }
}
