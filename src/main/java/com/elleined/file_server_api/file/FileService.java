package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface FileService {
    FileDTO save(@NotBlank UUID folder,
                 @NotNull MultipartFile file) throws IOException;

    void deleteByName(@NotBlank UUID folder,
                      @NotBlank String file) throws IOException;

    File getByName(@NotBlank UUID folder,
                   @NotBlank String file) throws IOException;
}
