package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("//folders/{folder}/files")
public class FileController {
    private final FileService fileService;

    private final Tika tika;

    @GetMapping("/{file:.+}")
    ResponseEntity<StreamingResponseBody> getByName(@PathVariable("folder") String folder,
                                                    @PathVariable("file") String file) throws IOException {

        File file = fileService.get(folder, file);
        if (!file.exists())
            throw new FileServerAPIException("File not exists");

        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush(); // Flush the output stream
                }
            } catch (IOException ex) {
                System.out.println("Streaming the file failed! " + ex.getMessage());
            }
        };

        String contentType = tika.detect(file);
        String contentDisposition = contentType.startsWith("image/")
                ? "inline"
                : "attachment";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + file + "\"")
                .contentLength(file.length())
                .contentType(MediaType.valueOf(contentType))
                .body(responseBody);
    }

    @PostMapping
    public String save(@PathVariable("folder") String folder,
                       @RequestPart("file") MultipartFile file) throws IOException {

        return fileService.save(folder, file);
    }

    @DeleteMapping("/{file:.+}")
    public void delete(@PathVariable("folder") String folder,
                       @PathVariable("file") String file) throws IOException {

        fileService.delete(folder, file);
    }

    @PutMapping("/{oldFile:.+}")
    public String update(@PathVariable("folder") String folder,
                         @PathVariable("oldFile") String oldFile,
                         @RequestPart("file") MultipartFile file) throws IOException {

        return fileService.update(folder, oldFile, file);;
    }
}
