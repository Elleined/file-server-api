package com.elleined.file_server_api.folder;

import java.io.IOException;
import java.nio.file.Path;

public interface FolderUtil {

    static boolean isNotInUploadPath(Path destinationPath, Path folderPath) {
        return !folderPath.startsWith(destinationPath);
    }

    Path getUploadPath() throws IOException;
}
