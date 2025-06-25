package com.elleined.file_server_api.folder;

import jakarta.validation.constraints.NotBlank;

import java.nio.file.Path;

public interface FolderService {
    Path save(@NotBlank String folder);
    void delete(@NotBlank String folder);
}
