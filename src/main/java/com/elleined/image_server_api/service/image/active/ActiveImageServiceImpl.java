package com.elleined.image_server_api.service.image.active;

import com.elleined.image_server_api.exception.image.ImageSizeException;
import com.elleined.image_server_api.exception.resource.ResourceAlreadyExistsException;
import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.image.Image;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.repository.image.ActiveImageRepository;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import com.elleined.image_server_api.request.ImageRequest;
import com.elleined.image_server_api.service.image.ImageFormatService;
import com.elleined.image_server_api.service.project.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActiveImageServiceImpl implements ActiveImageService {
    private final ProjectService projectService;

    private final ActiveImageRepository activeImageRepository;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageRepository deletedImageRepository;
    private final DeletedImageMapper deletedImageMapper;

    private final ImageFormatService imageFormatService;

    @Override
    public ActiveImage save(MultipartFile image, ImageRequest imageRequest) throws IOException {
        String uuid = imageRequest.getUuid();
        String description = imageRequest.getDescription();
        String additionalInformation = imageRequest.getAdditionalInformation();
        byte[] bytes = image.getBytes();
        Project project = projectService.getById(imageRequest.getProjectId());
        ImageFormat imageFormat = imageFormatService.getById(imageRequest.getImageFormatId());

        if (isAboveMaxFileSize(image))
            throw new ImageSizeException(STR."Cannot upload image! because image exceeds to file size which is \{MAX_FILE_SIZE}");

        if (isUUIDExists(uuid))
            throw new ResourceAlreadyExistsException("Cannot upload image! because uuid already exists!");

        ActiveImage activeImage = activeImageMapper.toEntity(uuid, description, additionalInformation, imageFormat, bytes, project);
        activeImageRepository.save(activeImage);
        log.debug("Uploading image success!");
        return activeImage;
    }

    @Override
    public ActiveImage getByUUID(String uuid) {
        ActiveImage activeImage = activeImageRepository.fetchByUUID(uuid).orElseThrow(() -> new ResourceNotFoundException(STR."Image with uuid of \{uuid} does not exists!"));

        activeImage.setLastAccessedAt(LocalDateTime.now());
        activeImageRepository.save(activeImage);

        return activeImage;
    }

    @Override
    public void deleteByUUID(String uuid) {
        ActiveImage activeImage = activeImageRepository.fetchByUUID(uuid).orElseThrow(() -> new ResourceNotFoundException(STR."Image with uuid of \{uuid} does not exists!"));
        DeletedImage deletedImage = deletedImageMapper.toEntity(activeImage);

        deletedImageRepository.save(deletedImage);
        activeImageRepository.delete(activeImage);
        log.debug("Deleting image with uuid of {} success", uuid);
    }

    @Override
    public ActiveImage restore(DeletedImage deletedImage) {
        ActiveImage activeImage = activeImageMapper.toEntity(deletedImage);

        activeImageRepository.save(activeImage);
        deletedImageRepository.delete(deletedImage);
        log.debug("Deleted image with uuid of {} restored successfully", deletedImage.getUuid());
        return activeImage;
    }

    @Override
    public List<ActiveImage> getAllById(List<Integer> ids) {
        List<ActiveImage> activeImages = activeImageRepository.findAllById(ids).stream()
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();

        activeImages.forEach(activeImage -> activeImage.setLastAccessedAt(LocalDateTime.now()));
        activeImageRepository.saveAll(activeImages);

        return activeImages;
    }

    @Override
    public boolean isUUIDExists(String uuid) {
        return activeImageRepository.findAll().stream()
                .map(Image::getUuid)
                .anyMatch(uuid::equalsIgnoreCase);
    }
}
