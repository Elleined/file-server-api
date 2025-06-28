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
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;

/*
    normalize method: removes the unnecessary dot(.) and slash(/, \) of specified folder
    toAbsolutePath method: get the absolute path of the specified path
    getFileName method: only get the last entry of specified folder
*/

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Async
    @Override
    public void create(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();

        // 1. Check for null or blank of specified folder
        if (this.isNullOrBlank(folder))
            throw new FileServerAPIException("Folder name cannot be null or blank");

        // 2. Check if specified folder size
        final int maxLength = 20;
        if (this.hasInvalidLength(folder, maxLength))
            throw new FileServerAPIException("Folder name cannot be longer than " + maxLength + " characters");

        // 3. Check if specified folder contain invalid characters
        if (!this.isAlphaNumeric(folder))
            throw new FileServerAPIException("Folder name cannot contain invalid characters");

        // 4. Normalize the folder
        Path folderPath = this.normalize(uploadPath, folder);

        // 5. Check if folderPath is symlink
        if (this.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Symbolic links are not allowed");

        // 6. Checks if the folderPath is within the upload directory
        if (!this.isInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Attempted traversal attack");

        // 7. Finally check if the folder name already exists
        if (this.exists(folderPath))
            throw new FileServerAPIException("Folder name already exists");

        Files.createDirectories(folderPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-r--r--")));
        log.info("Folder created successfully {}", folderPath.toAbsolutePath());
    }

    @Async
    @Override
    public void remove(String folder) throws IOException {
        Path uploadPath = this.getUploadPath();
        Path sanitizeFolder = Paths.get(folder.strip())
                .getFileName()
                .normalize();
        System.out.println("Raw " + folder);
        System.out.println("Sanitize " + sanitizeFolder);
        Path folderPath = uploadPath.resolve(sanitizeFolder).normalize();

        if (!folderPath.startsWith(uploadPath))
            throw new FileServerAPIException("Attempted traversal attack");

        if (folderPath.equals(uploadPath))
            throw new FileServerAPIException("Cannot delete root upload folder");

        if (Files.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Symbolic links are not allowed");

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
    public Path get(String folder) throws IOException {
        return null;
    }

    @Override
    public Path getUploadPath() throws IOException {
        return Paths.get(uploadPath.strip())
                .toAbsolutePath()
                .normalize()
                .toRealPath(LinkOption.NOFOLLOW_LINKS);
    }
}
