package com.elleined.file_server_api.service.file.active.local;

import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

public interface LocalActiveFileService {

    String save(Project project, Folder folder, MultipartFile file) throws IOException;
    byte[] getImage(Project project, Folder folder, String fileName) throws IOException;
    void transfer(Project project, Folder folder, MultipartFile file) throws IOException;
    void saveFailedUpload(Project project, Folder folder, MultipartFile file) throws IOException;

    default String getUniqueFileName(MultipartFile file) {
        String currentDateAndTime = LocalDateTime.now().toString();
        String fileName = file.getOriginalFilename();
        return currentDateAndTime + "_" + fileName;
    }
}
