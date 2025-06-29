package com.elleined.file_server_api.file;

import com.elleined.file_server_api.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FolderService folderService;

    @Override
    public String save(String folder,
                       MultipartFile file) throws IOException {

        Path folderPath = folderService.getByName(folder);

    }

    @Async
    @Override
    public void deleteByName(String folder,
                             String file) throws IOException {

    }

    @Override
    public File getByName(String folder,
                          String file) throws IOException {
        return null;
    }
}
