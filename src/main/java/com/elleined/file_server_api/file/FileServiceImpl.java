package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.folder.FolderService;
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
    public FileDTO save(String folder,
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
        String mimeType = tika.detect(file.getInputStream());
        List<String> allowedMimeTypes = List.of("image/png", "image/jpeg", "image/gif", "application/pdf");
        if (!allowedMimeTypes.contains(mimeType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + String.join(", ", allowedMimeTypes));

        // Check if file extension and mime type match
        Map<String, String> mimeTypeMap = Map.of(
                "image/jpeg", "jpg",
                "image/png", "png",
                "image/gif", "gif",
                "application/pdf", "pdf"
        );
        String realFileExtension = mimeTypeMap.get(mimeType);
        if (!extension.equals(realFileExtension))
            throw new FileServerAPIException("File upload failed! file extension and mime type do not match");

        // Check if file extension is allowed
        if (!allowedFileExtensions.contains(realFileExtension))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + String.join(", ", allowedMimeTypes));

        // Building the real file name
        String fileName = UUID.randomUUID() + "." + realFileExtension;

        // Resolving the file path for saving
        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(fileName).normalize();

        // Re-encoding the file to remove embedded code
        if (mimeType.startsWith("image/")) { // (Image Flattening)
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, realFileExtension, filePath.toFile());
        } else { // (PDF Flattening)
        }

        // Checksum checking (for deduplication and prevent tampering)

        // Set permission to 644 for rw-r--r--
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");
        Files.setPosixFilePermissions(filePath, permissions);

        return new FileDTO(fileName, realFileExtension, "", mimeType);
    }

    @Async
    @Override
    public void deleteByName(String folder,
                             String file) throws IOException {

    }

    @Override
    public File getByName(String folder,
                          String file) throws IOException {
        return null;
    }
}
