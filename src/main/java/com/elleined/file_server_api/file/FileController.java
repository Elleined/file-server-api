package com.elleined.file_server_api.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders/{folder}/files")
public class FileController {
    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String save(@PathVariable("folder") String folder,
                       @RequestPart("file") MultipartFile file) throws IOException {

        return fileService.save(folder, file);
    }

    @DeleteMapping("/{file:.+}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteByName(@PathVariable("folder") String folder,
                             @PathVariable("file") String file) throws IOException {

        fileService.deleteByName(folder, file);
    }
}
