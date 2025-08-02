package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FolderService folderService;

    private final FileUtil fileUtil;

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

        UUID fileId = UUID.randomUUID();

        String realExtension = fileUtil.getFileExtension(realMediaType);
        String fileName = fileUtil.getFileName(fileId, realExtension);

        Path folderPath = folderService.getByName(folder);
        Path filePath = fileUtil.resolve(folderPath, fileName);
        System.out.println(filePath);
        Files.createFile(filePath);

        String checksum = fileUtil.checksum(filePath);
        log.info("File saved successfully: {}", fileName);
        return new FileDTO(folder, fileId, realExtension, realMediaType, checksum);
    }

    @Override
    public Optional<FileEntity> getByUUID(UUID folder,
                                          UUID file) throws IOException, MimeTypeException {

        Path folderPath = folderService.getByName(folder);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            for (Path filePath : stream) {
                if (filePath.getFileName().toString().startsWith(file.toString())) {
                    MediaType mediaType = MediaType.parseMediaType(tika.detect(filePath));
                    String extension = fileUtil.getFileExtension(mediaType);

                    log.info("Fetching file metadata for {} success", file);
                    return Optional.of(new FileEntity(filePath, file, extension, mediaType));
                }
            }
        }
        return Optional.empty();
    }
}
