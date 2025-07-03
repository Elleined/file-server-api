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
    public UUID save() throws IOException {
        UUID folder = UUID.randomUUID();

        Path uploadPath = this.getUploadPath();
        Path folderPath = FolderUtil.normalize(uploadPath, folder);

        if (FolderUtil.isNotInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Folder creation failed! attempted traversal attack!");

        if (Files.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Folder creation failed! symbolic links are not allowed");

        if (Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS))
            throw new FileServerAPIException("Folder creation failed! folder name already exists");

        Files.createDirectories(folderPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
        log.info("Folder created successfully {}", folder);
        return folder;
    }

    @Async
    @Override
    public void deleteByName(UUID folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path folderPath = FolderUtil.normalize(uploadPath, folder)
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        if (FolderUtil.isNotInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Folder removal failed! attempted traversal attack");

        if (folderPath.equals(uploadPath))
            throw new FileServerAPIException("Folder removal failed! cannot delete root upload folder");

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
    public Path getByName(UUID folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path folderPath = FolderUtil.normalize(uploadPath, folder)
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        if (FolderUtil.isNotInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Folder retrieving failed! attempted traversal attack");

        return folderPath;
    }

    @Override
    public Path getUploadPath() throws IOException {
        return Paths.get(uploadPath.strip())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);
    }
}
