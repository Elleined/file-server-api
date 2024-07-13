package com.elleined.image_server_api.service.image.deleted.db;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import com.elleined.image_server_api.service.image.deleted.local.LocalDeletedImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DBDeletedImageServiceImpl implements DBDeletedImageService {
    private final DeletedImageRepository deletedImageRepository;

    private final ProjectService projectService;

    private final LocalDeletedImageService localDeletedImageService;

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
    public Page<DeletedImage> getAll(Project project, Folder folder, Pageable pageable) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get all deleted images! because this project doesn't have this folder!");

        return deletedImageRepository.findAll(folder, pageable);
    }

    @Override
    public void permanentlyDeleteDeletedImages() throws IOException {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        List<DeletedImage> deletedImages = deletedImageRepository.findAll().stream()
                .filter(deletedImage -> deletedImage.getLastAccessedAt().isBefore(oneMonthAgo) ||
                        deletedImage.getLastAccessedAt().equals(oneMonthAgo))
                .toList();

        localDeletedImageService.permanentlyDeleteDeletedImages(deletedImages);
        deletedImageRepository.deleteAll(deletedImages);
    }
}
