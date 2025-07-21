package com.elleined.file_server_api.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.util.UUID;

public record FileEntity(
        @JsonIgnore Path filePath,
        UUID fileId,
        String extension,
        String mediaType
) {

    public FileEntity(Path filePath,
                      UUID fileId,
                      String extension,
                      MediaType mediaType) {

        this(filePath, fileId, extension, mediaType.toString());
    }

    public String getContentDisposition() {
        return mediaType.startsWith("image")
                ? "inline"
                : "attachment";
    }

    public String getFileName() {
        return fileId.toString() + "." + extension;
    }
}
