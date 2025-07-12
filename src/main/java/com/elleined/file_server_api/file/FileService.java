package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface FileService {
    FileDTO save(@NotNull UUID folder,
                 @NotNull MultipartFile file) throws NoSuchAlgorithmException, IOException, MimeTypeException;

    MultipartFile getByName(@NotNull UUID folder,
                            @NotNull UUID file) throws IOException;

    boolean isChecksumMatched(@NotNull UUID folder,
                              @NotNull UUID file,
                              @NotBlank String checksum) throws IOException, NoSuchAlgorithmException;
}
