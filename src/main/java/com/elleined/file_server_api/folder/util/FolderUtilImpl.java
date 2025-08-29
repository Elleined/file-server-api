package com.elleined.file_server_api.folder.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FolderUtilImpl implements FolderUtil {

    private final String uploadPath;

    public FolderUtilImpl(@Value("${upload-path}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public Path getUploadPath() throws IOException {
        return Paths.get(this.uploadPath.strip()).toRealPath(LinkOption.NOFOLLOW_LINKS);
    }
}
