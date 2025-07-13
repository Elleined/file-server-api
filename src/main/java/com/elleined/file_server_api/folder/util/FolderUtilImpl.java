package com.elleined.file_server_api.folder.util;

import com.elleined.file_server_api.exception.FileServerAPIException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FolderUtilImpl implements FolderUtil {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    public Path getUploadPath() throws IOException, FileServerAPIException {
        final Path path = Paths.get(uploadPath.strip())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        final Set<PosixFilePermission> permissions = Set.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE
        );

        Set<PosixFilePermission> currentPermissions = Files.getPosixFilePermissions(path, LinkOption.NOFOLLOW_LINKS);
        if (!currentPermissions.equals(permissions))
            throw new FileServerAPIException("Upload path must have 700 permissions");

        return path;
    }
}
