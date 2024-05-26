package com.elleined.image_server_api.service.image;

import com.elleined.image_server_api.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadDirectory = "/images";

    String save(Project project, MultipartFile image) throws IOException;
    void delete(Project project, String fileName);
    byte[] getImage(Project project, String fileName);

    String getUniqueFileName(MultipartFile image);

    void createFolders(Project project) throws IOException;
}
