package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderUtil folderUtil;

    @Override
    public UUID save() throws IOException {
        Path folderPath = folderUtil.getUploadPath()
                .resolve(UUID.randomUUID().toString())
                .normalize();

        Path folder = Files.createDirectory(folderPath, PosixFilePermissions.asFileAttribute(
                PosixFilePermissions.fromString("rwx------"))
        ).toRealPath(LinkOption.NOFOLLOW_LINKS);

        log.info("Folder created successfully {}", folder);
        return UUID.fromString(folder.getFileName().toString());
    }

    @Override
    public Path getByName(UUID folder) throws IOException {
        return folderUtil.getUploadPath()
                .resolve(folder.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);
    }

}
