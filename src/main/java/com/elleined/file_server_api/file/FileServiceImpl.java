package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FolderService folderService;
    private final Tika tika;

    @Value("${MAX_FILE_SIZE_IN_MB}")
    private DataSize maxFileSize;

    // refactor this class for allowed mime types and file extensions and get file extension
    // refactor the fileservicetestimpl for correct mime types and file extensions
    // refactor the fileutil properly and test it


    // when reading detect again the mimeype and fileextension

    // add documentation for functions used toRealPath, normalize, resolve, getFileName, strip, etc.
    // add documenation for Files, File, Path, Paths
    // add documenttion springframework.MediaType, org.apache.tika.mime.MimeType

    private static final List<MediaType> allowedMimeTypes = List.of(
            MediaType.IMAGE_PNG,
            MediaType.IMAGE_JPEG,
            MediaType.APPLICATION_PDF
    );
    private static final List<String> allowedExtensions = allowedMimeTypes.stream()
            .map(FileServiceImpl::getFileExtension)
            .toList();

    public static String getFileExtension(MediaType mediaType) {
        try {
            return MimeTypes.getDefaultMimeTypes()
                    .forName(mediaType.toString())
                    .getExtension()
                    .substring(1); // Remove the leading dot
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileDTO save(UUID folder,
                        MultipartFile file) throws NoSuchAlgorithmException, IOException, MimeTypeException {


        // Check file size limit
        if (file.getSize() > maxFileSize.toBytes())
            throw new FileServerAPIException("File upload failed! file size limit exceeded");

        // Check real mime type extension
        MediaType realMediaType = MediaType.parseMediaType(tika.detect(file.getInputStream()));
        if (!allowedMimeTypes.contains(realMediaType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: ");

        // Check if a real file extension is allowed
        String realExtension = FileServiceImpl.getFileExtension(realMediaType);
        if (!allowedExtensions.contains(realExtension))
            throw new FileServerAPIException("File upload failed! only the following file extensions are allowed: ");

        // Building the real file name
        UUID fileId = UUID.randomUUID();
        String fileName = fileId + "." + realExtension; // '.' is omitted in the realExtension

        System.out.println("Mime type: " + realMediaType);
        System.out.println("Real Extension: " + realExtension);
        System.out.println("File ID: " + fileId);
        System.out.println("File Name: " + fileName);

        // Resolve the file path for saving
        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(fileName).normalize();

        // Re-encoding the file to remove embedded code
        if (realMediaType.toString().startsWith("image/")) { // (Image Flattening)
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, realExtension, filePath.toFile());
        } else { // (PDF Flattening)
            FileUtil.flattenPDF(filePath, file);
        }

        // Set permission to 644 for rw-r--r--
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("r--------");
        Files.setPosixFilePermissions(filePath, permissions);

        log.info("File saved successfully: {}", fileName);
        return new FileDTO(folder, fileId, realExtension, realMediaType, FileUtil.checksum(file));
    }

    @Override
    public MultipartFile getByName(UUID folder,
                                   UUID file) {

        return null;
    }

    @Override
    public boolean isChecksumMatched(UUID folder,
                                     UUID file,
                                     String checksum) {

        return false;
    }
}
