package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface FileService {
    FileDTO save(UUID folder,
                 @NotNull MultipartFile file) throws NoSuchAlgorithmException, IOException;

    MultipartFile getByName(UUID folder,
                            @Size(max = 41) @NotBlank String file) throws IOException;

    boolean isChecksumMatched(UUID folder,
                              @Size(max = 41) @NotBlank String file,
                              @Size(max = 41) @NotBlank String checksum) throws IOException, NoSuchAlgorithmException;
}
