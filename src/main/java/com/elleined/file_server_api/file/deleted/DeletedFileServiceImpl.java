package com.elleined.file_server_api.file.deleted;

import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeletedFileServiceImpl implements DeletedFileService {
    private final FolderService folderService;

    @Override
    public void restore(String projectName, String folderName, String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty())
            return;

        Path destination = folderService.getActiveImagesPath(projectName, folderName).resolve(fileName);
        Path source = folderService.getDeletedImagesPath(projectName, folderName).resolve(fileName);

        Files.move(source, destination);
        log.debug("Transferring file {} from {} to {} success!", fileName, source, destination);    }
}
