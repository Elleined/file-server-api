package com.elleined.file_server_api.controller.file;

import com.elleined.file_server_api.exception.SystemException;
import com.elleined.file_server_api.service.file.active.ActiveFileService;
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
@RequestMapping("/projects/{projectName}/folders/{folderName}/active-images")
public class ActiveFileController {
    private final ActiveFileService activeFileService;

    private final Tika tika;

    @GetMapping("/{fileName:.+}")
    ResponseEntity<StreamingResponseBody> getByName(@PathVariable("projectName") String projectName,
                                                    @PathVariable("folderName") String folderName,
                                                    @PathVariable("fileName") String fileName) throws IOException {

        File file = activeFileService.getByName(projectName, folderName, fileName);

        if (!file.exists())
            throw new SystemException("File not exists");

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
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .contentType(MediaType.valueOf(contentType))
                .body(responseBody);
    }

    @PostMapping
    public String save(@PathVariable("projectName") String projectName,
                       @PathVariable("folderName") String folderName,
                       @RequestPart("file") MultipartFile file,
                       @RequestParam("fileName") String fileName) throws IOException {

        return activeFileService.save(projectName, folderName, file, fileName);
    }

    @DeleteMapping("/{fileName:.+}")
    public void deleteByName(@PathVariable("projectName") String projectName,
                             @PathVariable("folderName") String folderName,
                             @PathVariable("fileName") String fileName) throws IOException {

        activeFileService.delete(projectName, folderName, fileName);
    }

    @PutMapping("/{oldFileName:.+}")
    public String update(@PathVariable("projectName") String projectName,
                         @PathVariable("folderName") String folderName,
                         @PathVariable("oldFileName") String oldFileName,
                         @RequestPart("file") MultipartFile file,
                         @RequestParam("fileName") String fileName) throws IOException {

        activeFileService.update(projectName, folderName, oldFileName, file, fileName);
        return fileName;
    }
}
