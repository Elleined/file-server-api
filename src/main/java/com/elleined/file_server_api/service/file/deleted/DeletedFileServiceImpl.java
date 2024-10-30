package com.elleined.file_server_api.service.file.deleted;

import com.elleined.file_server_api.service.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeletedFileServiceImpl implements DeletedFileService {
    private final FolderService folderService;

    @Override
    public void restore(String projectName, String folderName, String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty())
            return;

        Path destination = folderService.getActiveImagesPath(projectName, folderName);
        Path destinationPath = destination.resolve(Objects.requireNonNull(fileName));

        Path source = folderService.getDeletedImagesPath(projectName, folderName);
        Path sourcePath = source.resolve(Objects.requireNonNull(fileName));

        Files.move(sourcePath, destinationPath);
        log.debug("Transferring file {} from {} to {} success!", fileName, sourcePath, destinationPath);
    }
}
