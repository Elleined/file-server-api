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

    @Value("${FOLDER_MAX_LENGTH}")
    private int maxLength;

    @Async
    @Override
    public void create(String folder) throws IOException {
        // Check for null or blank of specified folder
        if (FolderValidator.isNullOrBlank(folder))
            throw new FileServerAPIException("Folder name cannot be null or blank");

        // Check if specified folder size
        if (FolderValidator.hasInvalidLength(folder, maxLength))
            throw new FileServerAPIException("Folder name cannot be longer than " + maxLength + " characters");

        // Check if specified folder contain invalid characters
        if (!FolderValidator.isAlphaNumeric(folder))
            throw new FileServerAPIException("Folder name cannot contain invalid characters");

        // Getting the upload path
        Path uploadPath = this.getUploadPath();

        // Normalize the folder
        Path folderPath = FolderValidator.normalize(uploadPath, folder);

        // Check if folderPath is symlink
        if (FolderValidator.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Symbolic links are not allowed");

        // Checks if the folderPath is within the upload directory
        if (!FolderValidator.isInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Attempted traversal attack");

        // Finally check if the folder name already exists
        if (FolderValidator.exists(folderPath))
            throw new FileServerAPIException("Folder name already exists");

        Files.createDirectories(folderPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-r--r--")));
        log.info("Folder created successfully {}", folder);
    }

    @Async
    @Override
    public void remove(String folder) throws IOException {
        // Check for null or blank of specified folder
        if (FolderValidator.isNullOrBlank(folder))
            throw new FileServerAPIException("Folder name cannot be null or blank");

        // Check if specified folder size
        if (FolderValidator.hasInvalidLength(folder, maxLength))
            throw new FileServerAPIException("Folder name cannot be longer than " + maxLength + " characters");

        // Check if specified folder contain invalid characters
        if (!FolderValidator.isAlphaNumeric(folder))
            throw new FileServerAPIException("Folder name cannot contain invalid characters");

        //  Getting the upload path
        Path uploadPath = this.getUploadPath();

        // Normalize the folder
        Path folderPath = FolderValidator.normalize(uploadPath, folder);

        // Check if folderPath is symlink
        if (FolderValidator.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Symbolic links are not allowed");

        // Checks if the folderPath is within the upload directory
        if (!FolderValidator.isInUploadPath(uploadPath, folderPath))
            throw new FileServerAPIException("Attempted traversal attack");

        // Check if the folder to be deleted is the upload directory
        if (folderPath.equals(uploadPath))
            throw new FileServerAPIException("Cannot delete root upload folder");

        // Finally check if the folder does not exists
        if (!FolderValidator.exists(folderPath))
            throw new FileServerAPIException("Folder does not exists");

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
