package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders/{folder}/files")
public class FileController {
    private final FileService fileService;
    private final Tika tika;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FileDTO save(@PathVariable("folder") UUID folder,
                        @RequestPart("file") MultipartFile file) throws IOException, NoSuchAlgorithmException {

        return fileService.save(folder, file);
    }

    @DeleteMapping("/{file:.+}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteByName(@PathVariable("folder") UUID folder,
                             @PathVariable("file") String file) {

        fileService.deleteByName(folder, file);
    }

    @GetMapping("/{file.+}")
    public ResponseEntity<StreamingResponseBody> getByName(@PathVariable("folder") UUID folder,
                                                           @PathVariable("file") String file) throws IOException {

        MultipartFile fetchedFile = fileService.getByName(folder, file);
        StreamingResponseBody responseBody = FileService.stream(fetchedFile);

        String contentType = tika.detect(file);
        String contentDisposition = contentType.startsWith("image/")
                ? "inline"
                : "attachment";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + fetchedFile.getOriginalFilename() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(contentType))
                .body(responseBody);
    }

    @GetMapping("/{file.+}/verify-checksum")
    public boolean isChecksumMatched(@PathVariable("folder") UUID folder,
                                     @PathVariable("file") String file,
                                     @RequestParam("checksum") String checksum) throws IOException, NoSuchAlgorithmException {

        return fileService.isChecksumMatched(folder, file, checksum);
    }
}
