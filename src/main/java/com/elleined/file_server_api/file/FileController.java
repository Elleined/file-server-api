package com.elleined.file_server_api.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders/{folder}/files")
public class FileController {
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FileDTO save(@PathVariable("folder") UUID folder,
                        @RequestPart("file") MultipartFile file) throws IOException {

        try {
            return fileService.save(folder, file);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{file:.+}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteByName(@PathVariable("folder") UUID folder,
                             @PathVariable("file") String file) throws IOException {

        fileService.deleteByName(folder, file);
    }
}
