package com.elleined.file_server_api.service.file.active;

import com.elleined.file_server_api.service.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveFileServiceImpl implements ActiveFileService {
    private final FolderService folderService;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public String save(String projectName, String folderName, MultipartFile image) throws IOException {
        String uniqueFileName = this.getUniqueFileName(image);
        Path filePath = folderService.getActiveImagesPath(projectName, folderName).resolve(uniqueFileName);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving file named {} to {} storage success!", uniqueFileName, filePath);

        return uniqueFileName;
    }

    @Override
    public void delete(String projectName, String folderName, String fileName) throws IOException {
        Path destination = folderService.getDeletedImagesPath(projectName, folderName);
        Path destinationPath = destination.resolve(Objects.requireNonNull(fileName));

        Path source = folderService.getActiveImagesPath(projectName, folderName);
        Path sourcePath = source.resolve(Objects.requireNonNull(fileName));

        Files.move(sourcePath, destinationPath);
        log.debug("Transferring file {} from {} to {} success!", fileName, sourcePath, destinationPath);
    }
}
