package com.elleined.file_server_api.file.deleted;

import jakarta.validation.constraints.NotBlank;

import java.io.IOException;

public interface DeletedFileService {
    void restore(@NotBlank String projectName,
                 @NotBlank String folderName,
                 @NotBlank String fileName) throws IOException;
}
