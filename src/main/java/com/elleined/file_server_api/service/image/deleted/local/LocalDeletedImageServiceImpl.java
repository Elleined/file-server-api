package com.elleined.file_server_api.service.image.deleted.local;

import com.elleined.file_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.file_server_api.model.PrimaryKeyUUID;
import com.elleined.file_server_api.model.file.DeletedFile;
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
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocalDeletedImageServiceImpl implements LocalDeletedImageService {
    private final ProjectService projectService;
    private final FolderService folderService;

    @Override
    public byte[] getImage(Project project, Folder folder, String fileName) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get image from storage! because this project doesn't have the specified upload folder");

        Path imagePath = Path.of(folderService.getDeletedImagesPath(project, folder).toString(), fileName);
        if (!Files.exists(imagePath))
            return null;

        return Files.readAllBytes(imagePath);
    }

    @Override
    public void transfer(Project project, Folder folder, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return;

        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot transfer image from storage! because this project doesn't have the specified upload folder");

        Path destination = folderService.getActiveImagesPath(project, folder);
        Path destinationPath = destination.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(destinationPath);

        Path source = folderService.getDeletedImagesPath(project, folder);
        Path sourcePath = source.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.delete(sourcePath);
        log.debug("Transferring image to {} success!", destinationPath);
    }

    @Override
    public void permanentlyDeleteDeletedImages(List<DeletedFile> deletedImages) throws IOException {
        List<Path> deletedImagePaths = deletedImages.stream()
                .map(deletedImage -> {
                    Folder folder = deletedImage.getFolder();
                    Project project = folder.getProject();

                    Path deletedImagePath = folderService.getDeletedImagesPath(project, folder);
                    return deletedImagePath.resolve(Objects.requireNonNull(deletedImagePath.getFileName()));
                })
                .toList();

        for (Path deletedImagePath : deletedImagePaths) {
            Files.delete(deletedImagePath);
        }
        log.debug("Permanently deleted delete images with ids of {}", deletedImages.stream().map(PrimaryKeyUUID::getId).toList());
    }
}
