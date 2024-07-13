package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.CustomMultipartFile;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.folder.FolderService;
import com.elleined.image_server_api.service.image.active.db.DBActiveImageService;
import com.elleined.image_server_api.service.image.active.local.LocalActiveImageService;
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
@RequestMapping("/projects/{projectId}/folders/{folderId}/active-images")
public class ActiveImageController {
    private final ProjectService projectService;

    private final FolderService folderService;

    private final LocalActiveImageService localActiveImageService;
    private final DBActiveImageService DBActiveImageService;
    private final ActiveImageMapper activeImageMapper;

    @GetMapping("/{uuid}")
    public ActiveImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                    @PathVariable("folderId") int folderId,
                                    @PathVariable("uuid") UUID uuid,
                                    @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);
        ActiveImage activeImage = DBActiveImageService.getByUUID(project, folder, uuid);

        byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes).addLinks(includeRelatedLinks);
    }

    @PostMapping
    public ActiveImageDTO save(@PathVariable("projectId") int projectId,
                               @PathVariable("folderId") int folderId,
                               @RequestPart("image") MultipartFile image,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "additionalInformation", required = false) String additionalInformation,
                               @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);

        ActiveImage activeImage = DBActiveImageService.save(project, folder, description, additionalInformation, image);

        byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes).addLinks(includeRelatedLinks);
    }

    @GetMapping
    public Page<ActiveImageDTO> getAll(@PathVariable("projectId") int projectId,
                                       @PathVariable("folderId") int folderId,
                                       @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                       @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                       @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                       @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy,
                                       @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);

        return DBActiveImageService.getAll(project, folder, pageable)
                .map(activeImage -> {
                    byte[] bytes;
                    try {
                        bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return activeImageMapper.toDTO(activeImage, bytes);
                })
                .map(dto -> dto.addLinks(includeRelatedLinks));
    }


    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectId") int projectId,
                             @PathVariable("folderId") int folderId,
                             @PathVariable("uuid") UUID uuid) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);
        ActiveImage activeImage = DBActiveImageService.getByUUID(project, folder, uuid);
        DBActiveImageService.deleteByUUID(project, folder, activeImage);

        String fileName = activeImage.getFileName();
        MultipartFile activeImageFile = new CustomMultipartFile(fileName, localActiveImageService.getImage(project, folder, fileName));
        localActiveImageService.transfer(project, folder, activeImageFile);
    }
}
