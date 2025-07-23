package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
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
    private final FileUtil fileUtil;

    @PostMapping
    public FileDTO save(@PathVariable("folder") UUID folder,
                        @RequestPart("file") MultipartFile file) throws IOException, NoSuchAlgorithmException, MimeTypeException, FileServerAPIException {

        return fileService.save(folder, file);
    }

    @GetMapping("/{file}")
    public ResponseEntity<StreamingResponseBody> getByName(@PathVariable("folder") UUID folder,
                                                           @PathVariable("file") UUID file) throws IOException, FileServerAPIException, MimeTypeException {

        FileEntity fileEntity = fileService.getByName(folder, file);
        StreamingResponseBody response = fileUtil.stream(fileEntity.filePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, fileEntity.getContentDisposition() + "; filename=\"" + fileEntity.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(fileEntity.mediaType()))
                .body(response);
    }

    @GetMapping("/{file}/verify")
    public boolean isChecksumMatched(@PathVariable("folder") UUID folder,
                                     @PathVariable("file") UUID file,
                                     @RequestParam("checksum") String checksum) throws IOException, NoSuchAlgorithmException, FileServerAPIException, MimeTypeException {

        FileEntity fetchedFile = fileService.getByName(folder, file);
        String filePathChecksum = fileUtil.checksum(fetchedFile.filePath());

        return filePathChecksum.equals(checksum);
    }
}
