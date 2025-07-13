package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.flattener.FileFlattener;
import com.elleined.file_server_api.file.util.FileUtil;
import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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
    private final FileUtil fileUtil;
    private final FileFlattener fileFlattener;

    private final Tika tika;

    @Value("${MAX_FILE_SIZE_IN_MB}")
    private DataSize maxFileSize;

    private static final List<MediaType> allowedMimeTypes = List.of(
            MediaType.IMAGE_PNG,
            MediaType.IMAGE_JPEG,
            MediaType.APPLICATION_PDF
    );

    @Override
    public FileDTO save(UUID folder,
                        MultipartFile file) throws NoSuchAlgorithmException, IOException {


        if (file.getSize() > maxFileSize.toBytes())
            throw new FileServerAPIException("File upload failed! file size limit exceeded");

        MediaType realMediaType = MediaType.parseMediaType(tika.detect(file.getInputStream()));
        if (!allowedMimeTypes.contains(realMediaType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: ");

        String realExtension = fileUtil.getFileExtension(realMediaType);

        UUID fileId = UUID.randomUUID();
        String fileName = fileId + "." + realExtension; // '.' is omitted in the realExtension

        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(fileName).normalize();

        if (realMediaType.toString().startsWith("image/")) {
            fileFlattener.flattenImage(filePath, file, realExtension);
        } else {
            fileFlattener.flattenPDF(filePath, file);
        }

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("r--------");
        Files.setPosixFilePermissions(filePath, permissions);

        String checksum = fileUtil.checksum(file);

        log.info("File saved successfully: {}", fileName);
        return new FileDTO(folder, fileId, realExtension, realMediaType, checksum);
    }

    @Override
    public MultipartFile getByName(UUID folder,
                                   UUID file) throws IOException {

        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(file.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

// when reading detect again the mimeype and fileextension
        return null;
    }

    @Override
    public boolean isChecksumMatched(UUID folder,
                                     UUID file,
                                     String checksum) throws IOException, NoSuchAlgorithmException {

        MultipartFile fetchedFile = this.getByName(folder, file);
        return fileUtil.checksum(fetchedFile).equals(checksum);
    }
}
