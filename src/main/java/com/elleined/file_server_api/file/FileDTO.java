package com.elleined.file_server_api.file;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileDTO(
        LocalDateTime uploadedAt,
        UUID folder,
        String name,
        String extension,
        String mimeType,
        String checksum
) {

    public FileDTO(UUID folder,
                   String name,
                   String extension,
                   String mimeType,
                   String checksum) {

        this(LocalDateTime.now(), folder, name, extension, mimeType, checksum);
    }
}
