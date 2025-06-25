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
    public String save(String projectName,
                       String folderName,
                       MultipartFile file,
                       String fileName) throws IOException {

        Path filePath = folderService.getActiveImagesPath(projectName, folderName).resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving file named {} to {} storage success!", fileName, filePath);

        return fileName;
    }

    @Override
    public File get(String projectName,
                    String folderName,
                    String fileName) {

        return Path.of(uploadPath)
                .resolve(projectName)
                .resolve("active")
                .resolve(folderName)
                .resolve(fileName)
                .toFile();
    }

    @Override
    public void update(String projectName,
                       String folderName,
                       String oldFileName,
                       MultipartFile file,
                       String fileName) throws IOException {

        Path filePath = folderService.getActiveImagesPath(projectName, folderName).resolve(fileName);
        if (Files.exists(filePath))
            return;

        this.save(projectName, folderName, file, fileName);
        this.delete(projectName, folderName, oldFileName);
    }

    @Override
    public void delete(String projectName,
                       String folderName,
                       String fileName) throws IOException {

        Path destination = folderService.getDeletedImagesPath(projectName, folderName).resolve(fileName);
        Path source = folderService.getActiveImagesPath(projectName, folderName).resolve(fileName);

        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Transferring file {} from {} to {} success!", fileName, source, destination);
    }

    @Override
    public void restore(String projectName,
                        String folderName,
                        String fileName) throws IOException {

        Path destination = folderService.getActiveImagesPath(projectName, folderName).resolve(fileName);
        Path source = folderService.getDeletedImagesPath(projectName, folderName).resolve(fileName);

        Files.move(source, destination);
        log.debug("Transferring file {} from {} to {} success!", fileName, source, destination);
    }
}
