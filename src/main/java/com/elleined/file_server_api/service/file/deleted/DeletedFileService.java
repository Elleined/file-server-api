package com.elleined.file_server_api.service.file.deleted;

import java.io.IOException;

public interface DeletedFileService {
    void restore(String projectName, String folderName, String fileName) throws IOException;
}
