package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public String save() throws IOException {
        String folder = UUID.randomUUID().toString();

        Path uploadPath = this.getUploadPath();
        Path folderPath = FolderValidator.normalize(uploadPath, folder);

        if (FolderValidator.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Folder creation failed! symbolic links are not allowed");

        if (FolderValidator.isNotInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Folder creation failed! attempted traversal attack!");

        if (FolderValidator.exists(folderPath))
            throw new FileServerAPIException("Folder creation failed! folder name already exists");

        Files.createDirectories(folderPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-r--r--")));
        log.info("Folder created successfully {}", folder);

        return folder;
    }

    @Async
    @Override
    public void deleteByName(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path folderPath = FolderValidator.normalize(uploadPath, folder);

        if (FolderValidator.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Folder removal failed! symbolic links are not allowed");

        if (FolderValidator.isNotInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Folder removal failed! attempted traversal attack");

        if (folderPath.equals(uploadPath))
            throw new FileServerAPIException("Folder removal failed! cannot delete root upload folder");

        if (!FolderValidator.exists(folderPath))
            throw new FileServerAPIException("Folder removal failed! folder does not exists");

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

        log.info("Folder deleted successfully {}", folder);
    }

    @Override
    public Path getByName(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path folderPath = FolderValidator.normalize(uploadPath, folder);

        if (FolderValidator.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Folder retrieving failed! symbolic links are not allowed");

        if (FolderValidator.isNotInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Folder retrieving failed! attempted traversal attack");

        if (!FolderValidator.exists(folderPath))
            throw new FileServerAPIException("Folder retrieving failed! folder does not exists");

        return folderPath;
    }

    @Override
    public Path getUploadPath() throws IOException {
        return Paths.get(uploadPath.strip())
                .toAbsolutePath()
                .normalize()
                .toRealPath(LinkOption.NOFOLLOW_LINKS);
    }
}
