package com.elleined.file_server_api.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public record FileDTO(
        @JsonProperty("uploaded_at") LocalDateTime uploadedAt,
        @JsonProperty("folder") UUID folder,
        @JsonProperty("file_id") UUID fileId,
        @JsonProperty("extension") String extension,
        @JsonProperty("media_type") String mediaType,
        @JsonProperty("checksum") String checksum
) {

    public FileDTO(UUID folder,
                   UUID fileId,
                   String extension,
                   MediaType mediaType,
                   String checksum) {

        this(LocalDateTime.now(ZoneId.systemDefault()), folder, fileId, extension, mediaType.toString(), checksum);
    }

    @JsonProperty("file_name")
    public String getFileName() {
        return fileId + "." + extension;
    }
}