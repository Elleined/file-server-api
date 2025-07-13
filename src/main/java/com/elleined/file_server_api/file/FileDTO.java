package com.elleined.file_server_api.file;

import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.UUID;

public record FileDTO(
        LocalDateTime uploadedAt,
        UUID folder,
        UUID fileId,
        String extension,
        MediaType mediaType,
        String checksum
) {

    public FileDTO(UUID folder,
                   UUID fileId,
                   String extension,
                   MediaType mediaType,
                   String checksum) {

        this(LocalDateTime.now(), folder, fileId, extension, mediaType, checksum);
    }

    public String getFileName() {
        return fileId + "." + extension;
    }
}
