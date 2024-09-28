package com.elleined.file_server_api.service.file.active.local;

import com.elleined.file_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocalActiveFileServiceImpl implements LocalActiveFileService {
    private final ProjectService projectService;
    private final FolderService folderService;

    @Override
    public String save(Project project, Folder folder, MultipartFile image) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot save image to storage! because this project doesn't have the specified upload folder");

        String uniqueFileName = this.getUniqueFileName(image);
        Path uploadPath = folderService.getActiveImagesPath(project, folder);
        Path filePath = uploadPath.resolve(uniqueFileName);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving image to storage success!");
        return uniqueFileName;
    }

    @Override
    public byte[] getImage(Project project, Folder folder, String fileName) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get image from storage! because this project doesn't have the specified upload folder");

        Path imagePath = Path.of(folderService.getActiveImagesPath(project, folder).toString(), fileName);
        if (!Files.exists(imagePath))
            return null;

        return Files.readAllBytes(imagePath);
    }

    @Override
    public void transfer(Project project, Folder folder, MultipartFile multipartFile) throws IOException {
        if (!projectService.has(project, folder)) {
            throw new ResourceNotOwnedException("Cannot transfer image from storage! because this project doesn't have the specified upload folder");
        }

        if (multipartFile == null || multipartFile.isEmpty())
            return;

        Path destination = folderService.getDeletedImagesPath(project, folder);
        Path destinationPath = destination.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(destinationPath);

        Path source = folderService.getActiveImagesPath(project, folder);
        Path sourcePath = source.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Files.delete(sourcePath);
        log.debug("Transferring image from to {} success!", destinationPath);
    }

    @Override
    public void saveFailedUpload(Project project, Folder folder, MultipartFile image) throws IOException {
        String uniqueFileName = this.getUniqueFileName(image);
        Path uploadPath = folderService.getFailedUploadsPath(project, folder);
        Path filePath = uploadPath.resolve(uniqueFileName);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving image to storage success!");
    }
}
