package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.folder.util.FolderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Validated
public class FolderServiceImpl implements FolderService {
    private final FolderUtil folderUtil;
    private static final Logger log = LoggerFactory.getLogger(FolderServiceImpl.class);

    public FolderServiceImpl(FolderUtil folderUtil) {
        this.folderUtil = folderUtil;
    }

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

    @Override
    public void delete(UUID folder) throws IOException {
        Path folderPath = folderUtil.getUploadPath()
                .resolve(folder.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        Files.delete(folderPath);
        log.info("Folder deleted successfully {}", folder);
    }
}
