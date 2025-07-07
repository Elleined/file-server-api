package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
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
        UUID folder = UUID.randomUUID();

        Path uploadPath = folderUtil.getUploadPath();
        Path folderPath = uploadPath.resolve(folder.toString()).normalize();

        if (!folderUtil.isInUploadPath(folderPath))
            throw new FileServerAPIException("Folder creation failed! attempted traversal attack!");

        if (folderUtil.isSymbolicLink(folderPath))
            throw new FileServerAPIException("Folder creation failed! symbolic links are not allowed");

        if (folderUtil.exists(folderPath))
            throw new FileServerAPIException("Folder creation failed! folder name already exists");

        Files.createDirectory(folderPath, PosixFilePermissions.asFileAttribute(
                PosixFilePermissions.fromString("rwx------")));

        log.info("Folder created successfully {}", folder);
        return folder;
    }

    @Override
    public Path getByName(UUID folder) throws IOException {
        Path uploadPath = folderUtil.getUploadPath();
        Path folderPath = uploadPath.resolve(folder.toString())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        if (!folderUtil.isInUploadPath(folderPath))
            throw new FileServerAPIException("Folder retrieving failed! attempted traversal attack");

        return folderPath;
    }

}
