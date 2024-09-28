package com.elleined.file_server_api.service.image.active.db;

import com.elleined.file_server_api.exception.image.ImageSizeException;
import com.elleined.file_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.file_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.file_server_api.mapper.image.ActiveImageMapper;
import com.elleined.file_server_api.mapper.image.DeletedImageMapper;
import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.repository.image.ActiveImageRepository;
import com.elleined.file_server_api.repository.image.DeletedImageRepository;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.image.active.local.LocalActiveImageService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DBActiveImageServiceImpl implements DBActiveImageService {
    private final ProjectService projectService;

    private final LocalActiveImageService localActiveImageService;
    private final ActiveImageRepository activeImageRepository;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageRepository deletedImageRepository;
    private final DeletedImageMapper deletedImageMapper;

    private final FolderService folderService;

    @Override
    public ActiveFile save(Project project,
                           Folder folder,
                           String description,
                           String additionalInformation,
                           MultipartFile image) throws IOException {

        if (!projectService.has(project, folder)) {
            localActiveImageService.saveFailedUpload(project, folder, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ResourceNotOwnedException("Cannot upload image! because this project doesn't have the specified upload folder");
        }

        if (isAboveMaxFileSize(image)) {
            localActiveImageService.saveFailedUpload(project, folder, image); // Save the file anyways HAHAHA. If you don't want this just literally remove this line :)
            throw new ImageSizeException("Cannot upload image! because image exceeds to file size which is " + MAX_FILE_SIZE);
        }

        double fileSizeInMB = this.getSizeInMB(image);
        String fileName = localActiveImageService.save(project, folder, image);

        ActiveFile activeImage = activeImageMapper.toEntity(description, additionalInformation, fileName, folder, fileSizeInMB);
        activeImageRepository.save(activeImage);
        log.debug("Uploading image success!");
        return activeImage;
    }

    @Override
    public ActiveFile getByUUID(Project project, Folder folder, UUID uuid) {
        ActiveFile activeImage = activeImageRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Image with uuid of " + uuid + " does not exists!"));

        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get by uuid! because this project doesn't have the specified upload folder");

        if (!folderService.has(folder, activeImage))
            throw new ResourceNotOwnedException("Cannot get by uuid! because project does not owned this image!");

        activeImage.setLastAccessedAt(LocalDateTime.now());
        activeImageRepository.save(activeImage);

        return activeImage;
    }

    @Override
    public Page<ActiveFile> getAll(Project project, Folder folder, Pageable pageable) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get all active images! because this project doesn't have this folder!");

        return activeImageRepository.findAll(folder, pageable);
    }

    @Override
    public void deleteByUUID(Project project, Folder folder, ActiveFile activeImage) {
        if (!projectService.has(project, folder)) {
            throw new ResourceNotOwnedException("Cannot delete by uuid! because this project doesn't have the specified upload folder");
        }

        if (!folderService.has(folder, activeImage))
            throw new ResourceNotOwnedException("Cannot delete by uuid! because project does not owned this image!");

        DeletedFile deletedImage = deletedImageMapper.toEntity(activeImage);

        deletedImageRepository.save(deletedImage);
        activeImageRepository.delete(activeImage);
        log.debug("Deleting image with uuid of {} success", activeImage.getId());
    }

    @Override
    public ActiveFile restore(Project project, Folder folder, DeletedFile deletedImage) {
        if (!projectService.has(project, folder)) {
            throw new ResourceNotOwnedException("Cannot restore image! because this project doesn't have the specified upload folder");
        }

        if (!folderService.has(folder, deletedImage))
            throw new ResourceNotOwnedException("Cannot restore image! because this project does not owned this image!");

        ActiveFile activeImage = activeImageMapper.toEntity(deletedImage);

        activeImageRepository.save(activeImage);
        deletedImageRepository.delete(deletedImage);
        log.debug("Deleted image with uuid of {} restored successfully", deletedImage.getId());
        return activeImage;
    }
}
