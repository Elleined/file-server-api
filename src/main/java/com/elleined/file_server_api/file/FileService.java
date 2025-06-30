package com.elleined.file_server_api.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public interface FileService {
    FileDTO save(@NotBlank UUID folder,
                 @NotNull MultipartFile file) throws NoSuchAlgorithmException, IOException;

    void deleteByName(@NotBlank UUID folder,
                      @NotBlank String file);

    MultipartFile getByName(@NotBlank UUID folder,
                            @NotBlank String file);

    boolean isChecksumMatched(@NotBlank UUID folder,
                              @NotBlank String file,
                              @NotBlank String checksum) throws IOException, NoSuchAlgorithmException;


    static String computeChecksum(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[8192]; // 8kb
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hash = digest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }
}
