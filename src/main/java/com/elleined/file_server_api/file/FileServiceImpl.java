package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FolderService folderService;
    private final Tika tika;

    @Value("${MAX_FILE_SIZE_IN_MB}")
    private DataSize maxFileSize;

    private static final List<String> allowedMimeTypes = List.of("image/png", "image/jpeg", "application/pdf");
    private static final List<String> allowedExtensions = List.of("png", "jpg", "jpeg", "pdf");
    private static final Map<String, String> mimeTypes = Map.of(
            "image/jpeg", "jpeg",
            "image/png", "png",
            "application/pdf", "pdf"
    );

    @Override
    public FileDTO save(UUID folder,
                        MultipartFile file) throws NoSuchAlgorithmException, IOException {

        // Check file size limit
        if (file.getSize() > maxFileSize.toBytes())
            throw new FileServerAPIException("File upload failed! file size limit exceeded");

        // Check for multiple file extensions
        String[] parts = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        if (parts.length > 2)
            throw new FileServerAPIException("File upload failed! multiple extensions are not allowed");

        // Check if the declared file extension is allowed
        String extension = parts[1].toLowerCase();
        if (!allowedExtensions.contains(extension))
            throw new FileServerAPIException("File upload failed! only the following file extensions are allowed: " + String.join(", ", allowedExtensions));

        // Check if the declared mime type is allowed
        String mimeType = Objects.requireNonNull(file.getContentType()).toLowerCase();
        if (!allowedMimeTypes.contains(mimeType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + String.join(", ", allowedMimeTypes));

        // Check real mime type extension
        String realMimeType = tika.detect(file.getInputStream()).toLowerCase();
        if (!allowedMimeTypes.contains(realMimeType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + String.join(", ", allowedMimeTypes));

        // Check if the declared mime type matches the real mime type
        if (!Objects.requireNonNull(mimeType).equals(realMimeType))
            throw new FileServerAPIException("File upload failed! declared mime type does not match the real mime type");

        // Check if file extension and mime type match
        String realExtension = mimeTypes.get(realMimeType).toLowerCase();
        if (!extension.equals(realExtension))
            throw new FileServerAPIException("File upload failed! declared file extension does not match the real file extension");

        // Check if a real file extension is allowed
        if (!allowedExtensions.contains(realExtension))
            throw new FileServerAPIException("File upload failed! only the following file extensions are allowed: " + String.join(", ", allowedExtensions));

        // Building the real file name
        String fileName = UUID.randomUUID() + "." + realExtension;

        // Resolving the file path for saving
        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(fileName).normalize();

        // Re-encoding the file to remove embedded code
        if (realMimeType.startsWith("image/")) { // (Image Flattening)
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, realExtension, filePath.toFile());
        } else { // (PDF Flattening)
            FileUtil.flattenPDF(filePath, file);
        }

        // Set permission to 644 for rw-r--r--
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("r--------");
        Files.setPosixFilePermissions(filePath, permissions);

        log.info("File saved successfully: {}", fileName);
        return new FileDTO(folder, fileName, realExtension, realMimeType, FileUtil.checksum(file));
    }

    @Override
    public MultipartFile getByName(UUID folder,
                                   String file) throws IOException {

        // Resolving the file path for saving
        Path folderPath = folderService.getByName(folder);
        Path normalizePath = Paths.get(file.strip())
                .getFileName()
                .normalize();
        Path filePath = folderPath.resolve(normalizePath).normalize();

        // Checking if file exists
        if (!Files.exists(filePath, LinkOption.NOFOLLOW_LINKS))
            throw new FileServerAPIException("File reading failed! file does not exists");

        return null;
    }

    @Override
    public boolean isChecksumMatched(UUID folder,
                                     String file,
                                     String checksum) throws IOException, NoSuchAlgorithmException {

        MultipartFile fetchedFile = this.getByName(folder, file);
        String fetchedFileChecksum = FileUtil.checksum(fetchedFile);
        return fetchedFileChecksum.equals(checksum);
    }
}
