package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.flattener.FileFlattener;
import com.elleined.file_server_api.file.util.FileUtil;
import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

    private static final List<MediaType> allowedMimeTypes = List.of(
            MediaType.IMAGE_PNG,
            MediaType.IMAGE_JPEG,
            MediaType.APPLICATION_PDF
    );

    @Override
    public FileDTO save(UUID folder,
                        MultipartFile file) throws NoSuchAlgorithmException, IOException, MimeTypeException, FileServerAPIException {

        MediaType realMediaType = MediaType.parseMediaType(tika.detect(file.getInputStream()));
        if (!allowedMimeTypes.contains(realMediaType))
            throw new FileServerAPIException("File upload failed! only the following mime types are allowed: " + allowedMimeTypes);

        String realExtension = fileUtil.getFileExtension(realMediaType);

        UUID fileId = UUID.randomUUID();
        String fileName = fileId + "." + realExtension;

        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(fileName).normalize();

        if (realMediaType.toString().startsWith("image/")) {
            fileFlattener.flattenImage(filePath, file, realExtension);
        } else {
            fileFlattener.flattenPDF(filePath, file);
        }

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("r--------");
        Files.setPosixFilePermissions(filePath, permissions);

        String checksum = fileUtil.checksum(filePath);

        log.info("File saved successfully: {}", fileName);
        return new FileDTO(folder, fileId, realExtension, realMediaType, checksum);
    }

    @Override
    public FileMetaData getByName(UUID folder,
                                  UUID file) throws IOException, FileServerAPIException, MimeTypeException {

        Path folderPath = folderService.getByName(folder);
        Path filePath = folderPath.resolve(file.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        MediaType mediaType = MediaType.parseMediaType(tika.detect(filePath));
        String extension = fileUtil.getFileExtension(mediaType);

        log.info("Fetching file metadata for: {} success", file);
        return new FileMetaData(filePath, file, extension, mediaType);
    }

    @Override
    public boolean isChecksumMatched(UUID folder,
                                     UUID file,
                                     String checksum) throws IOException, NoSuchAlgorithmException, FileServerAPIException, MimeTypeException {

        FileMetaData fetchedFile = this.getByName(folder, file);

        Path filePath = fetchedFile.filePath();
        String filePathChecksum = fileUtil.checksum(filePath);

        return filePathChecksum.equals(checksum);
    }
}
