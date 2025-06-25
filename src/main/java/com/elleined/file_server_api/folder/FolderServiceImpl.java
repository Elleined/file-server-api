package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public Path save(String folder) {
        return this.sanitize(folder);
    }

    @Override
    public void delete(String folder) {

    }

    @Override
    public Path getUploadPath() {
        return this.sanitize(uploadPath);
    }
}
