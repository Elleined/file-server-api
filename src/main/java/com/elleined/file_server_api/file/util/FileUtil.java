package com.elleined.file_server_api.file.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.security.NoSuchAlgorithmException;

public interface FileUtil {

    // '.' is omitted in the realExtension
    String getFileExtension(MediaType mediaType);

    String checksum(MultipartFile file) throws NoSuchAlgorithmException;

    StreamingResponseBody stream(MultipartFile file);
}
