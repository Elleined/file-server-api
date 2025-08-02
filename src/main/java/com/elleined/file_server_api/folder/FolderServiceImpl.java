package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.folder.util.FolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderUtil folderUtil;

    @Override
    public UUID save() throws IOException {
        UUID folderName = UUID.randomUUID();
        Path folderPath = folderUtil.getUploadPath()
                .resolve(folderName.toString())
                .normalize();

        Files.createDirectory(folderPath);
        log.info("Folder created successfully {}", folderName);
        return folderName;
    }

    @Override
    public Path getByName(UUID folder) throws IOException {
        return folderUtil.getUploadPath()
                .resolve(folder.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);
    }
}
