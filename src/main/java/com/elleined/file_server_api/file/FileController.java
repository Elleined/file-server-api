package com.elleined.file_server_api.file;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders/{folder}/files")
public class FileController {
    private final FileService fileService;
    private final Tika tika;

    @PostMapping
    public FileDTO save(@PathVariable("folder") UUID folder,
                        @RequestPart("file") MultipartFile file) throws IOException, NoSuchAlgorithmException, MimeTypeException {

        return fileService.save(folder, file);
    }

    @GetMapping("/{file.+}")
    public ResponseEntity<StreamingResponseBody> getByName(@PathVariable("folder") UUID folder,
                                                           @PathVariable("file") UUID file) throws IOException {

        MultipartFile fetchedFile = fileService.getByName(folder, file);

        String contentType = tika.detect(fetchedFile.getInputStream());
        String contentDisposition = contentType.startsWith("image/")
                ? "inline"
                : "attachment";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=\"" + fetchedFile.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(FileUtil.stream(fetchedFile));
    }

    @GetMapping("/{file.+}/verify-checksum")
    public boolean isChecksumMatched(@PathVariable("folder") UUID folder,
                                     @PathVariable("file") UUID file,
                                     @RequestParam("checksum") String checksum) throws IOException, NoSuchAlgorithmException {

        return fileService.isChecksumMatched(folder, file, checksum);
    }
}
