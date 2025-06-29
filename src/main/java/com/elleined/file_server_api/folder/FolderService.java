package com.elleined.file_server_api.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.nio.file.Path;

public interface FolderService {
    void save(@Size(max = 25)
              @NotBlank
              @Pattern(regexp = "^[a-zA-Z0-9]+$") String folder) throws IOException;

    void deleteByName(@Size(max = 25)
                      @NotBlank
                      @Pattern(regexp = "^[a-zA-Z0-9]+$") String folder) throws IOException;

    Path getByName(@Size(max = 25)
                   @NotBlank
                   @Pattern(regexp = "^[a-zA-Z0-9]+$") String folder) throws IOException;

    Path getUploadPath() throws IOException;
}
