package com.elleined.file_server_api.service.file.active;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ActiveFileService {
    String save(String projectName, String folderName, MultipartFile file) throws IOException;
    void delete(String projectName, String folderName, String fileName) throws IOException;

    default String getUniqueFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }

    static String getContentType(String filename) {
        return switch (filename.substring(filename.lastIndexOf('.') + 1).toLowerCase()) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            default -> "application/octet-stream";
        };
    }
}
