package com.elleined.file_server_api.file.active;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ActiveFileService {
    void delete(String projectName, String folderName, String fileName) throws IOException;

    File getByName(String projectName, String folderName, String fileName);

    String save(String projectName, String folderName, MultipartFile file, String fileName) throws IOException;
    void update(String projectName, String folderName, String oldFileName, MultipartFile file, String fileName) throws IOException;
}
