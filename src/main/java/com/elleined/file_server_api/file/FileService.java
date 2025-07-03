package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public interface FileService {
    FileDTO save(@NotBlank UUID folder,
                 @NotNull MultipartFile file) throws NoSuchAlgorithmException, IOException;

    void deleteByName(@NotBlank UUID folder,
                      @Size(max = 41) @NotBlank String file);

    MultipartFile getByName(@NotBlank UUID folder,
                            @Size(max = 41) @NotBlank String file) throws IOException;

    boolean isChecksumMatched(@NotBlank UUID folder,
                              @Size(max = 41) @NotBlank String file,
                              @Size(max = 41) @NotBlank String checksum) throws IOException, NoSuchAlgorithmException;
}
