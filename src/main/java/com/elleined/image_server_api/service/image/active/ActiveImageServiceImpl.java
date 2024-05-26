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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActiveImageServiceImpl implements ActiveImageService {
    private final ProjectService projectService;

    private final ImageService imageService;

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

        if (isAboveMaxFileSize(image))
            throw new ImageSizeException(STR."Cannot upload image! because image exceeds to file size which is \{MAX_FILE_SIZE}");

        if (!imageFormatService.isFileExtensionValid(image))
            throw new ImageFormatException("Cannot upload image! because extension name is not valid. Please refer to valid extension names!");

        ImageFormat imageFormat = imageFormatService.getByMultipart(image).orElseThrow(() -> new ResourceNotFoundException("Cannot upload image! format is not valid!"));
        String fileName = imageService.save(project, image);

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
    public void deleteByUUID(Project project, UUID uuid) {
        ActiveImage activeImage = activeImageRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException(STR."Image with uuid of \{uuid} does not exists!"));

        if (!projectService.has(project, activeImage))
            throw new ResourceNotOwnedException("Project does not owned this image!");

        DeletedImage deletedImage = deletedImageMapper.toEntity(activeImage);
        imageService.delete(project, deletedImage.getFileName());

        deletedImageRepository.save(deletedImage);
        activeImageRepository.delete(activeImage);
        log.debug("Deleting image with uuid of {} success", uuid);
    }

    @Override
    public ActiveImage restore(Project project, DeletedImage deletedImage) {
        ActiveImage activeImage = activeImageMapper.toEntity(deletedImage);

        if (!projectService.has(project, activeImage))
            throw new ResourceNotOwnedException("Project does not owned this image!");

        activeImageRepository.save(activeImage);
        deletedImageRepository.delete(deletedImage);
        log.debug("Deleted image with uuid of {} restored successfully", deletedImage.getId());
        return activeImage;
    }

    @Override
    public List<ActiveImage> getAllByUUID(List<UUID> uuids) {
        List<ActiveImage> activeImages = activeImageRepository.findAllById(uuids).stream()
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();

        activeImages.forEach(activeImage -> activeImage.setLastAccessedAt(LocalDateTime.now()));
        activeImageRepository.saveAll(activeImages);

        return activeImages;
    }
}
