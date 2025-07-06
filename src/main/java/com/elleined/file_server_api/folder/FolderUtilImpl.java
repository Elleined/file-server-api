package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FolderUtilImpl implements FolderUtil {

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    public Path getUploadPath() throws IOException {
        return Paths.get(uploadPath.strip())
                .toRealPath(LinkOption.NOFOLLOW_LINKS);
    }
}
