package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public void create(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path sanitizeFolder = Paths.get(folder)
                .getFileName()
                .normalize();

        Path folderPath = uploadPath.resolve(sanitizeFolder).normalize();
        if (!folderPath.startsWith(uploadPath))
            throw new FileServerAPIException("Attempted traversal attack");

        if (Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS))
            throw new FileServerAPIException("Folder already exists");

        Files.createDirectories(folderPath);
        log.info("Folder created successfully {}", folderPath.toAbsolutePath());
    }

    @Override
    public void remove(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path sanitizeFolder = Paths.get(folder)
                .getFileName()
                .normalize();

        Path folderPath = uploadPath.resolve(sanitizeFolder).normalize();
        if (!folderPath.startsWith(uploadPath))
            throw new FileServerAPIException("Attempted traversal attack");

        if (folderPath.equals(uploadPath))
            throw new FileServerAPIException("Cannot delete root upload folder");

        if (!Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS))
            throw new FileServerAPIException("Folder does not exist");

        Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

        log.info("Folder deleted successfully {}", folderPath.toAbsolutePath());
    }

    @Override
    public Path getUploadPath() {
        return Paths.get(uploadPath.strip())
                .toAbsolutePath()
                .normalize();
    }
}
