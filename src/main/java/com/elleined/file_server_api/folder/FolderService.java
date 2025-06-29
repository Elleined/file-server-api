package com.elleined.file_server_api.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.nio.file.Path;

public interface FolderService {
    void create(@Size(max = 25) @NotBlank String folder) throws IOException;
    void remove(@NotBlank String folder) throws IOException;
    Path get(@NotBlank String folder) throws IOException;
    Path getUploadPath() throws IOException;
}
