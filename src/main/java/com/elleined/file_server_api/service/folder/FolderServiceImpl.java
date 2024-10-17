package com.elleined.file_server_api.service.folder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FolderServiceImpl implements FolderService {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public Path getUploadPath() {
        return Path.of(uploadPath);
    }
}
