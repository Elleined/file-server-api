package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Async
    @Override
    public void save(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path folderPath = uploadPath.resolve(folder);

        if (!folderPath.startsWith(uploadPath))
            throw new FileServerAPIException("Attempted traversal attack");

        if (Files.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Symbolic links are not allowed");

        if (Files.exists(folderPath))
            throw new FileServerAPIException("Folder already exists");

        Files.createDirectories(folderPath);
    }

    @Async
    @Override
    public void delete(String folder) {

    }

    @Override
    public Path getUploadPath() {
        return Paths.get(uploadPath.strip())
                .normalize()
                .toAbsolutePath()
                .normalize();
    }
}
