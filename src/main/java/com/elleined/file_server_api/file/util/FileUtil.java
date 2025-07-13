package com.elleined.file_server_api.file.util;

import org.apache.tika.mime.MimeTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public interface FileUtil {

    // '.' is omitted in the realExtension
    String getFileExtension(MediaType mediaType) throws MimeTypeException;

    String checksum(Path file) throws NoSuchAlgorithmException, IOException;

    StreamingResponseBody stream(Path file);
}
