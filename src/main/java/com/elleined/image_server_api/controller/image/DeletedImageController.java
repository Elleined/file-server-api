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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    @GetMapping("/get-all-by-uuid")
    public List<DeletedImageDTO> getAllByUUID(@PathVariable("projectId") int projectId,
                                              @PathVariable("folderId") int folderId,
                                              @RequestBody List<String> uuids) {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);
        List<UUID> ids = uuids.stream()
                .map(UUID::fromString)
                .toList();

        return DBDeletedImageService.getAllByUUID(project, folder, ids).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }

    @PutMapping("/{uuid}/restore")
    public ActiveImageDTO restore(@PathVariable("projectId") int projectId,
                                  @PathVariable("folderId") int folderId,
                                  @PathVariable("uuid") String uuid) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);
        DeletedImage deletedImage = DBDeletedImageService.getByUUID(project, folder, UUID.fromString(uuid));
        ActiveImage activeImage = DBActiveImageService.restore(project, folder, deletedImage);

        String fileName = deletedImage.getFileName();
        MultipartFile deletedImageFile = new CustomMultipartFile(fileName, localDeletedImageService.getImage(project, folder, fileName));
        localDeletedImageService.transfer(project, folder, deletedImageFile);

        byte[] bytes = localDeletedImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @GetMapping("/{uuid}")
    public DeletedImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                     @PathVariable("folderId") int folderId,
                                     @PathVariable("uuid") String uuid) {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);
        DeletedImage deletedImage = DBDeletedImageService.getByUUID(project, folder, UUID.fromString(uuid));
        return deletedImageMapper.toDTO(deletedImage);
    }
}
