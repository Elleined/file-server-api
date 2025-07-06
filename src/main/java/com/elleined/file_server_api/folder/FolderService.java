package com.elleined.file_server_api.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface FolderService {
    UUID save() throws IOException;

    void deleteByName(@Size(max = 36)
                      @NotBlank UUID folder) throws IOException;

    Path getByName(@Size(max = 36)
                   @NotBlank UUID folder) throws IOException;
}
