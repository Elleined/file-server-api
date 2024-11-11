package com.elleined.file_server_api.service.file.active;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface ActiveFileService {
    void delete(String projectName, String folderName, String fileName) throws IOException;

    File getByName(String projectName, String folderName, String fileName);

    String save(String projectName, String folderName, MultipartFile file, String fileName) throws IOException;
}
