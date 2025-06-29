package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    String save(@NotBlank String folder,
                @NotNull MultipartFile file) throws IOException;

    void delete(@NotBlank String folder,
                @NotBlank String file) throws IOException;

    File get(@NotBlank String folder,
             @NotBlank String file) throws IOException;
}
