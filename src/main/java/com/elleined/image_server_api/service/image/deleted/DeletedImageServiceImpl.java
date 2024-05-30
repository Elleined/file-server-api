package com.elleined.image_server_api.service.image.deleted;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier("deletedImageServiceImpl")
public class DeletedImageServiceImpl implements DeletedImageService {
    private final DeletedImageRepository deletedImageRepository;

    private final ProjectService projectService;

    @Override
    public List<DeletedImage> getAllByUUID(Project project, Folder folder, List<UUID> uuids) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get all by uuid! because this project doesn't have the specified upload folder");

        List<DeletedImage> deletedImages = deletedImageRepository.findAllById(uuids).stream()
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();

        deletedImages.forEach(deletedImage -> deletedImage.setLastAccessedAt(LocalDateTime.now()));
        deletedImageRepository.saveAll(deletedImages);

        return deletedImages;
    }

    @Override
    public DeletedImage getByUUID(Project project, Folder folder, UUID uuid) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get image by uuid! because this project doesn't have the specified upload folder");

        DeletedImage deletedImage = deletedImageRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException(STR."Deleted image with uuid of \{uuid} does not exists!"));

        deletedImage.setLastAccessedAt(LocalDateTime.now());
        deletedImageRepository.save(deletedImage);

        return deletedImage;
    }

    @Override
    public void deleteAll() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        List<DeletedImage> deletedImages = deletedImageRepository.findAll().stream()
                .filter(deletedImage -> deletedImage.getLastAccessedAt().isBefore(oneMonthAgo) ||
                        deletedImage.getLastAccessedAt().equals(oneMonthAgo))
                .toList();
        deletedImageRepository.deleteAll(deletedImages);
    }

    @Override
    public String save(Project project, Folder folder, MultipartFile image) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot save image to storage! because this project doesn't have the specified upload folder");

        String uniqueFileName = this.getUniqueFileName(image);
        Path uploadPath = Path.of(this.getDeletedImagesPath(project));
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saving image to local storage success!");
        return uniqueFileName;
    }

    @Override
    public byte[] getImage(Project project, Folder folder, String fileName) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get image from storage! because this project doesn't have the specified upload folder");

        Path imagePath = Path.of(this.getDeletedImagesPath(project), fileName);
        if (!Files.exists(imagePath))
            return null;

        return Files.readAllBytes(imagePath);
    }

    @Override
    public void transfer(Project project, Folder folder, MultipartFile multipartFile) throws IOException {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot transfer image from storage! because this project doesn't have the specified upload folder");

        if (multipartFile == null || multipartFile.isEmpty())
            return;

        Path destination = Path.of(this.getActiveImagesPath(project));
        Path destinationPath = destination.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        multipartFile.transferTo(destinationPath);
        log.debug("Transferring image to {} success!", destinationPath);
    }
}
