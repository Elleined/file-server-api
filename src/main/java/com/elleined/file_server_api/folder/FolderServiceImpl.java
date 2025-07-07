package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    
    private final FolderUtil folderUtil;

    @Override
    public UUID save() throws IOException {
        UUID folder = UUID.randomUUID();

        Path uploadPath = folderUtil.getUploadPath();
        Path folderPath = uploadPath.resolve(folder.toString()).normalize();

        if (!folderUtil.isInUploadPath(folderPath))
            throw new FileServerAPIException("Folder creation failed! attempted traversal attack!");

        if (folderUtil.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Folder creation failed! symbolic links are not allowed");

        if (folderUtil.exists(folderPath))
            throw new FileServerAPIException("Folder creation failed! folder name already exists");

        Files.createDirectory(folderPath, PosixFilePermissions.asFileAttribute(
                PosixFilePermissions.fromString("rwx------")));

        log.info("Folder created successfully {}", folder);
        return folder;
    }

    @Async
    @Override
    public void deleteByName(UUID folder) throws IOException {
        Path uploadPath = folderUtil.getUploadPath();
        Path folderPath = uploadPath.resolve(folder.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        if (!folderUtil.isInUploadPath(folderPath))
            throw new FileServerAPIException("Folder removal failed! attempted traversal attack");

        if (folderPath.equals(uploadPath))
            throw new FileServerAPIException("Folder removal failed! cannot delete root upload folder");

        Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                log.debug("File deleted successfully {}", file.getFileName());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                log.debug("Directory deleted successfully {}", dir.getFileName());
                return FileVisitResult.CONTINUE;
            }
        });

        log.info("Folder deleted successfully {}", folder);
    }

    @Override
    public Path getByName(UUID folder) throws IOException {
        Path uploadPath = folderUtil.getUploadPath();
        Path folderPath = uploadPath.resolve(folder.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        if (!folderUtil.isInUploadPath(folderPath))
            throw new FileServerAPIException("Folder retrieving failed! attempted traversal attack");

        return folderPath;
    }

}
