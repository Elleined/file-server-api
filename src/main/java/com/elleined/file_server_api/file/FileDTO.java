package com.elleined.file_server_api.file;

import java.time.LocalDateTime;

public record FileDTO(
        LocalDateTime uploadedAt,
        String fileName,
        String fileExtension,
        String checksum,
        String mimeType
) {

    public FileDTO(String fileName,
                   String fileExtension,
                   String checksum,
                   String mimeType) {

        this(LocalDateTime.now(), fileName, fileExtension, checksum, mimeType);
    }
}
