package com.elleined.file_server_api.controller.file;

import com.elleined.file_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.file_server_api.service.file.active.ActiveFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders/{folderName}/active-images")
public class ActiveFileController {
    private final ActiveFileService activeFileService;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @GetMapping("/{fileName:.+}")
    ResponseEntity<StreamingResponseBody> getByName(@PathVariable("projectName") String projectName,
                                                    @PathVariable("folderName") String folderName,
                                                    @PathVariable("fileName") String fileName) {

        File file = Path.of(uploadPath)
                .resolve(projectName)
                .resolve("active")
                .resolve(folderName)
                .resolve(fileName)
                .toFile();

        if (!file.exists())
            throw new ResourceNotFoundException("File not exists");

        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush(); // Flush the output stream
                }
            } catch (IOException ignored) {
                System.out.println("Streaming file failed! ");
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.valueOf(ActiveFileService.getContentType(fileName)))
                .body(responseBody);
    }

    @PostMapping
    public String save(@PathVariable("projectName") String projectName,
                       @PathVariable("folderName") String folderName,
                       @RequestPart("file") MultipartFile file) throws IOException {

        return activeFileService.save(projectName, folderName, file);
    }

    @DeleteMapping("/{fileName:.+}")
    public void deleteByName(@PathVariable("projectName") String projectName,
                             @PathVariable("folderName") String folderName,
                             @PathVariable("fileName") String fileName) throws IOException {

        activeFileService.delete(projectName, folderName, fileName);
    }
}
