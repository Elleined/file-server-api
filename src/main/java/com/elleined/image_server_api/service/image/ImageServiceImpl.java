package com.elleined.image_server_api.service.image;

import com.elleined.image_server_api.model.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Override
    public String save(Project project, MultipartFile image) throws IOException {
        String uniqueFileName = getUniqueFileName(image);
        Path uploadPath = Path.of(uploadDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @Override
    public void delete(Project project, String fileName) {

    }

    @Override
    public byte[] getImage(Project project, String fileName) {
        return new byte[0];
    }

    @Override
    public String getUniqueFileName(MultipartFile image) {
        String currentDateAndTime = LocalDateTime.now().toString();
        String fileName = image.getOriginalFilename();
        return STR."\{currentDateAndTime}_\{fileName}";
    }

    @Override
    public void createFolders(Project project) throws IOException {
        Path projectDirectory = Path.of(STR."\{uploadDirectory}/\{project.getName()}");
        Path activeImagesDirectory = Path.of(STR."\{uploadDirectory}/\{project.getName()}/active");
        Path deletedImagesDirectory = Path.of(STR."\{uploadDirectory}/\{project.getName()}/deleted");

        if (!Files.exists(projectDirectory)) Files.createDirectory(projectDirectory);
        if (!Files.exists(activeImagesDirectory)) Files.createDirectory(activeImagesDirectory);
        if (!Files.exists(deletedImagesDirectory)) Files.createDirectory(deletedImagesDirectory);
    }
}
