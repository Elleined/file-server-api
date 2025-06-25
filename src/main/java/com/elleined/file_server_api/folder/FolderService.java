package com.elleined.file_server_api.folder;

import jakarta.validation.constraints.NotBlank;

import java.io.IOException;
import java.nio.file.Path;

public interface FolderService {
    void save(@NotBlank String folder) throws IOException;
    void delete(@NotBlank String folder);
    Path getUploadPath();
}
