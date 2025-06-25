package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    void delete(@NotBlank String projectName,
                @NotBlank String folderName,
                @NotBlank String fileName) throws IOException;

    File getByName(@NotBlank String projectName,
                   @NotBlank String folderName,
                   @NotBlank String fileName);

    String save(@NotBlank String projectName,
                @NotBlank String folderName,
                @NotNull MultipartFile file,
                @NotBlank String fileName) throws IOException;

    void update(@NotBlank String projectName,
                @NotBlank String folderName,
                @NotBlank String oldFileName,
                @NotNull MultipartFile file,
                @NotBlank String fileName) throws IOException;

    void restore(@NotBlank String projectName,
                 @NotBlank String folderName,
                 @NotBlank String fileName) throws IOException;
}
