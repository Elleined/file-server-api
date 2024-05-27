package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.exception.image.ImageFormatException;
import com.elleined.image_server_api.exception.image.ImageSizeException;
import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.repository.image.ActiveImageRepository;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import com.elleined.image_server_api.service.image.ImageService;
import com.elleined.image_server_api.service.image.format.ImageFormatService;
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

    private final ImageFormatService imageFormatService;

    @Override
    public ActiveImage save(Project project,
                            String description,
                            String additionalInformation,
                            MultipartFile image) throws IOException {

        if (isAboveMaxFileSize(image)) {
            this.saveFailedUpload(project, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ImageSizeException(STR."Cannot upload image! because image exceeds to file size which is \{MAX_FILE_SIZE}");
        }

        if (!imageFormatService.isFileExtensionValid(image)) {
            this.saveFailedUpload(project, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ImageFormatException("Cannot upload image! because extension name is not valid. Please refer to valid extension names!");
        }

        ImageFormat imageFormat = imageFormatService.getByMultipart(image)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot upload image! format is not valid!"));

        String fileName = this.save(project, image);
        ActiveImage activeImage = activeImageMapper.toEntity(description, additionalInformation, imageFormat, fileName, project);
        activeImageRepository.save(activeImage);
        log.debug("Uploading image success!");
        return activeImage;
    }

    @Override
    public ActiveImage getByUUID(Project project, UUID uuid) {
        ActiveImage activeImage = activeImageRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException(STR."Image with uuid of \{uuid} does not exists!"));

        if (!projectService.has(project, activeImage))
            throw new ResourceNotOwnedException("Project does not owned this image!");

        activeImage.setLastAccessedAt(LocalDateTime.now());
        activeImageRepository.save(activeImage);

        return activeImage;
    }

    @Override
    public void deleteByUUID(Project project, ActiveImage activeImage) {
        if (!projectService.has(project, activeImage))
            throw new ResourceNotOwnedException("Project does not owned this image!");

        DeletedImage deletedImage = deletedImageMapper.toEntity(activeImage);

        deletedImageRepository.save(deletedImage);
        activeImageRepository.delete(activeImage);
        log.debug("Deleting image with uuid of {} success", activeImage.getId());
    }

    @Override
    public ActiveImage restore(Project project, DeletedImage deletedImage) {
        if (!projectService.has(project, deletedImage))
            throw new ResourceNotOwnedException("Cannot restore image! because this project does not owned this image!");

        ActiveImage activeImage = activeImageMapper.toEntity(deletedImage);

        activeImageRepository.save(activeImage);
        deletedImageRepository.delete(deletedImage);
        log.debug("Deleted image with uuid of {} restored successfully", deletedImage.getId());
        return activeImage;
    }

    @Override
    public List<ActiveImage> getAllByUUID(Project project, List<UUID> uuids) {
        List<ActiveImage> activeImages = activeImageRepository.findAllById(uuids).stream()
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();

        activeImages.forEach(activeImage -> activeImage.setLastAccessedAt(LocalDateTime.now()));
        activeImageRepository.saveAll(activeImages);

        return activeImages;
    }

    @Override
    public String save(Project project, MultipartFile image) throws IOException {
        String uniqueFileName = this.getUniqueFileName(image);
        Path uploadPath = Path.of(this.getActiveImagesPath(project));
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving image to local storage success!");
        return uniqueFileName;
    }

    @Override
    public byte[] getImage(Project project, String fileName) throws IOException {
        Path imagePath = Path.of(this.getActiveImagesPath(project), fileName);
        if (!Files.exists(imagePath))
            return null;

        return Files.readAllBytes(imagePath);
    }

    @Override
    public void transfer(Project project, MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) return;
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
        log.debug("Saving image to local storage success!");
    }
}
