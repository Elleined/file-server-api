package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.folder.FolderService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FolderService folderService;
    private final Tika tika;

    @Override
    public FileDTO save(@NotBlank UUID folder,
                        MultipartFile file) throws IOException {

        // Check for multiple file extensions
        String[] parts = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        if (parts.length > 2)
            throw new FileServerAPIException("File upload failed! multiple extensions are not allowed");

        // Check if file extension is allowed
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        List<String> allowedFileExtensions = List.of("png", "jpg", "jpeg", "gif", "pdf");
        if (!allowedFileExtensions.contains(extension))
            throw new FileServerAPIException("File upload failed! only the following file extensions are allowed: " + String.join(", ", allowedFileExtensions));

        // Check file size limit

        // Check mime type extension
        String realMimeType = tika.detect(file.getInputStream());
        List<String> allowedMimeTypes = List.of("image/png", "image/jpeg", "image/gif", "application/pdf");
        if (!allowedMimeTypes.contains(realMimeType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + String.join(", ", allowedMimeTypes));

        // Check if file extension and mime type match
        Map<String, String> mimeTypeMap = Map.of(
                "image/jpeg", "jpg",
                "image/png", "png",
                "image/gif", "gif",
                "application/pdf", "pdf"
        );
        String realExtension = mimeTypeMap.get(realMimeType);
        if (!extension.equals(realExtension))
            throw new FileServerAPIException("File upload failed! file extension and mime type do not match");

        // Check if a real file extension is allowed
        if (!allowedFileExtensions.contains(realExtension))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + String.join(", ", allowedMimeTypes));

        // Building the real file name
        String fileName = UUID.randomUUID() + "." + realExtension;

        // Resolving the file path for saving
        Path folderPath = folderService.getByName(folder);
        Path normalizePath = Paths.get(fileName.strip())
                .getFileName()
                .normalize();
        Path filePath = folderPath.resolve(normalizePath).normalize();

        // Re-encoding the file to remove embedded code
        if (realMimeType.startsWith("image/")) { // (Image Flattening)
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, realExtension, filePath.toFile());
        } else { // (PDF Flattening)
        }

        // Checksum checking (for deduplication and prevent tampering)

        // Set permission to 644 for rw-r--r--
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
        Files.setPosixFilePermissions(filePath, permissions);

        return new FileDTO(fileName, realExtension, "", realMimeType);
    }

    @Async
    @Override
    public void deleteByName(@NotBlank UUID folder,
                             String file) throws IOException {

    }

    @Override
    public File getByName(@NotBlank UUID folder,
                          String file) throws IOException {
        return null;
    }
}
