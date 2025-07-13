package com.elleined.file_server_api.folder.util;

import com.elleined.file_server_api.exception.FileServerAPIException;

import java.io.IOException;
import java.nio.file.Path;

public interface FolderUtil {
    Path getUploadPath() throws IOException, FileServerAPIException;
}
