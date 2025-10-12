package com.elleined.file_server_api.file.flattener;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileFlattener {
    void flattenImage(Path filePath, MultipartFile file, String realExtension) throws IOException;
}
