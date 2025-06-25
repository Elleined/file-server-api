package com.elleined.file_server_api.file;

import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FolderService folderService;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public String save(String folder,
                       MultipartFile file) throws IOException {

        Path filePath = folderService.getActiveImagesPath(projectName, folder).resolve(file);

        Files.copy(attachment.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving file named {} to {} storage success!", file, filePath);

        return file;
    }

    @Override
    public File get(String folder,
                    String file) {

        return Path.of(uploadPath)
                .resolve(projectName)
                .resolve("active")
                .resolve(folder)
                .resolve(file)
                .toFile();
    }

    @Override
    public void update(String folder,
                       String oldFile,
                       MultipartFile file) throws IOException {

        Path filePath = folderService.getActiveImagesPath(projectName, folder).resolve(fileName);
        if (Files.exists(filePath))
            return;

        this.save(folder, fileName);
        this.delete(folder, oldFile);
    }

    @Override
    public void delete(String folder,
                       String file) throws IOException {

        Path destination = folderService.getDeletedImagesPath(projectName, folder).resolve(file);
        Path source = folderService.getActiveImagesPath(projectName, folder).resolve(file);

        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Transferring file {} from {} to {} success!", file, source, destination);
    }
}
