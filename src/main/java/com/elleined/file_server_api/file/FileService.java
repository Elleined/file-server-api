package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import jakarta.validation.constraints.NotNull;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

public interface FileService {
    FileDTO save(@NotNull UUID folder,
                 @NotNull MultipartFile file) throws NoSuchAlgorithmException, IOException, MimeTypeException, FileServerAPIException;

    Optional<FileEntity> getByUUID(@NotNull UUID folder,
                                   @NotNull UUID file) throws IOException, FileServerAPIException, MimeTypeException;
}
