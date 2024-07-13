package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.CustomMultipartFile;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.folder.FolderService;
import com.elleined.image_server_api.service.image.active.db.DBActiveImageService;
import com.elleined.image_server_api.service.image.deleted.db.DBDeletedImageService;
import com.elleined.image_server_api.service.image.deleted.local.LocalDeletedImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/folders/{folderId}/deleted-images")
public class DeletedImageController {
    private final ProjectService projectService;

    private final FolderService folderService;

    private final DBActiveImageService DBActiveImageService;
    private final ActiveImageMapper activeImageMapper;

    private final LocalDeletedImageService localDeletedImageService;
    private final DBDeletedImageService DBDeletedImageService;
    private final DeletedImageMapper deletedImageMapper;


    @GetMapping("/{uuid}")
    public DeletedImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                     @PathVariable("folderId") int folderId,
                                     @PathVariable("uuid") UUID uuid,
                                     @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);
        DeletedImage deletedImage = DBDeletedImageService.getByUUID(project, folder, uuid);
        return deletedImageMapper.toDTO(deletedImage);
    }

    @GetMapping
    public Page<DeletedImageDTO> getAll(@PathVariable("projectId") int projectId,
                                        @PathVariable("folderId") int folderId,
                                        @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                        @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                        @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                        @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy,
                                        @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return DBDeletedImageService.getAll(project, folder, pageable)
                .map(deletedImageMapper::toDTO);
    }

    @PutMapping("/{uuid}/restore")
    public ActiveImageDTO restore(@PathVariable("projectId") int projectId,
                                  @PathVariable("folderId") int folderId,
                                  @PathVariable("uuid") UUID uuid,
                                  @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);
        DeletedImage deletedImage = DBDeletedImageService.getByUUID(project, folder, uuid);

        ActiveImage activeImage = DBActiveImageService.restore(project, folder, deletedImage);

        String fileName = deletedImage.getFileName();
        MultipartFile deletedImageFile = new CustomMultipartFile(fileName, localDeletedImageService.getImage(project, folder, fileName));
        localDeletedImageService.transfer(project, folder, deletedImageFile);

        byte[] bytes = localDeletedImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }
}
