package com.elleined.file_server_api.file;

import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.util.UUID;

public record FileMetaData(
        Path filePath,
        UUID fileId,
        String extension,
        String mediaType
) {

    public FileMetaData(Path filePath,
                        UUID fileId,
                        String extension,
                        MediaType mediaType) {

        this(filePath, fileId, extension, mediaType.toString());
    }

    public String getContentDisposition() {
        return mediaType.startsWith("image/")
                ? "inline"
                : "attachment";
    }

    public String getFileName() {
        return fileId.toString() + "." + extension;
    }
}
