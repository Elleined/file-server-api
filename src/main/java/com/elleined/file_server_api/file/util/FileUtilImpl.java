package com.elleined.file_server_api.file.util;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FileUtilImpl implements FileUtil {

    @Override
    public String getFileExtension(MediaType mediaType) {
        try {
            return MimeTypes.getDefaultMimeTypes()
                    .forName(mediaType.toString())
                    .getExtension()
                    .substring(1); // Remove the leading dot
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checksum(MultipartFile file) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[8192]; // 8kb
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new FileServerAPIException("Error reading file for checksum " + e.getMessage());
        }

        byte[] hash = digest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    @Override
    public StreamingResponseBody stream(MultipartFile file) {
        return outputStream -> {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new FileServerAPIException("Error streaming file" + e.getMessage());
            }
        };
    }
}
