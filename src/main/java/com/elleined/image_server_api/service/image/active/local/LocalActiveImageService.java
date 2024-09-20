package com.elleined.image_server_api.service.image.active.local;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

public interface LocalActiveImageService {

    String save(Project project, Folder folder, MultipartFile image) throws IOException;
    byte[] getImage(Project project, Folder folder, String fileName) throws IOException;
    void transfer(Project project, Folder folder, MultipartFile multipartFile) throws IOException;
    void saveFailedUpload(Project project, Folder folder, MultipartFile image) throws IOException;

    default String getUniqueFileName(MultipartFile image) {
        String currentDateAndTime = LocalDateTime.now().toString();
        String fileName = image.getOriginalFilename();
        return currentDateAndTime + "_" + fileName;
    }
}
