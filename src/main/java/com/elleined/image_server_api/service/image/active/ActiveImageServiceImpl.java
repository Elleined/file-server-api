package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.exception.image.ImageFormatException;
import com.elleined.image_server_api.exception.image.ImageSizeException;
import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.format.Format;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.repository.image.ActiveImageRepository;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import com.elleined.image_server_api.service.folder.FolderService;
import com.elleined.image_server_api.service.format.FormatService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Primary
public class ActiveImageServiceImpl implements ActiveImageService {
    private final ProjectService projectService;

    private final ActiveImageRepository activeImageRepository;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageRepository deletedImageRepository;
    private final DeletedImageMapper deletedImageMapper;

    private final FolderService folderService;

    private final FormatService formatService;

    @Override
    public ActiveImage save(Project project,
                            Folder folder,
                            String description,
                            String additionalInformation,
                            MultipartFile image) throws IOException {

        if (!projectService.has(project, folder)) {
            this.saveFailedUpload(project, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ResourceNotOwnedException("Cannot upload image! because this project doesn't have the specified upload folder");
        }

        if (isAboveMaxFileSize(image)) {
            this.saveFailedUpload(project, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ImageSizeException(STR."Cannot upload image! because image exceeds to file size which is \{MAX_FILE_SIZE}");
        }

        if (!formatService.isFileExtensionValid(image)) {
            this.saveFailedUpload(project, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ImageFormatException("Cannot upload image! because extension name is not valid. Please refer to valid extension names!");
        }

        Format format = formatService.getByMultipart(image)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot upload image! format is not valid!"));

        String fileName = this.save(project, folder, image);
        ActiveImage activeImage = activeImageMapper.toEntity(description, additionalInformation, format, fileName, folder);
        activeImageRepository.save(activeImage);
        log.debug("Uploading image success!");
        return activeImage;
    }

    @Override
    public ActiveImage getByUUID(Project project, Folder folder, UUID uuid) {
        ActiveImage activeImage = activeImageRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException(STR."Image with uuid of \{uuid} does not exists!"));

        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get by uuid! because this project doesn't have the specified upload folder");

        if (!folderService.has(folder, activeImage))
            throw new ResourceNotOwnedException("Cannot get by uuid! because project does not owned this image!");

        activeImage.setLastAccessedAt(LocalDateTime.now());
        activeImageRepository.save(activeImage);

        return activeImage;
    }

    @Override
    public void deleteByUUID(Project project, Folder folder, ActiveImage activeImage) {
        if (!projectService.has(project, folder)) {
            throw new ResourceNotOwnedException("Cannot delete by uuid! because this project doesn't have the specified upload folder");
        }

        if (!folderService.has(folder, activeImage))
            throw new ResourceNotOwnedException("Cannot delete by uuid! because project does not owned this image!");

        DeletedImage deletedImage = deletedImageMapper.toEntity(activeImage);

        deletedImageRepository.save(deletedImage);
        activeImageRepository.delete(activeImage);
        log.debug("Deleting image with uuid of {} success", activeImage.getId());
    }

    @Override
    public ActiveImage restore(Project project, Folder folder, DeletedImage deletedImage) {
        if (!projectService.has(project, folder)) {
            throw new ResourceNotOwnedException("Cannot restore image! because this project doesn't have the specified upload folder");
        }

        if (!folderService.has(folder, deletedImage))
            throw new ResourceNotOwnedException("Cannot restore image! because this project does not owned this image!");

        ActiveImage activeImage = activeImageMapper.toEntity(deletedImage);

        activeImageRepository.save(activeImage);
        deletedImageRepository.delete(deletedImage);
        log.debug("Deleted image with uuid of {} restored successfully", deletedImage.getId());
        return activeImage;
    }

    @Override
    public List<ActiveImage> getAllByUUID(Project project, Folder folder, List<UUID> uuids) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get all by uuid! because this project doesn't have the specified upload folder");

        List<ActiveImage> activeImages = activeImageRepository.findAllById(uuids).stream()
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();

        activeImages.forEach(activeImage -> activeImage.setLastAccessedAt(LocalDateTime.now()));
        activeImageRepository.saveAll(activeImages);

        return activeImages;
    }

    @Override
    public String save(Project project, Folder folder, MultipartFile image) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot save image to storage! because this project doesn't have the specified upload folder");

        String uniqueFileName = this.getUniqueFileName(image);
        Path uploadPath = Path.of(this.getActiveImagesPath(project));
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving image to storage success!");
        return uniqueFileName;
    }

    @Override
    public byte[] getImage(Project project, Folder folder, String fileName) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get image from storage! because this project doesn't have the specified upload folder");

        Path imagePath = Path.of(this.getActiveImagesPath(project), fileName);
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

        Path destination = Path.of(this.getDeletedImagesPath(project));
        Path destinationPath = destination.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        multipartFile.transferTo(destinationPath);
        log.debug("Transferring image from to {} success!", destinationPath);
    }

    private void saveFailedUpload(Project project, MultipartFile image) throws IOException {
        String uniqueFileName = this.getUniqueFileName(image);
        Path uploadPath = Path.of(this.getFailedUploadsPath(project));
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving image to storage success!");
    }
}
