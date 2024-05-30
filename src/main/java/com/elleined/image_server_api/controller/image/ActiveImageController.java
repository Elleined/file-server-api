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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/folder/{folderId}/active-images")
public class ActiveImageController {
    private final ProjectService projectService;
    
    private final FolderService folderService;

    private final LocalActiveImageService localActiveImageService;
    private final DBActiveImageService DBActiveImageService;
    private final ActiveImageMapper activeImageMapper;

    @PostMapping
    public ActiveImageDTO save(@PathVariable("projectId") int projectId,
                               @PathVariable("folderId") int folderId,
                               @RequestPart("image") MultipartFile image,
                               @RequestPart(value = "description", required = false) String description,
                               @RequestPart(value = "additionalInformation", required = false) String additionalInformation) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);

        ActiveImage activeImage = DBActiveImageService.save(project, folder, description, additionalInformation, image);

        byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @GetMapping("/{uuid}")
    public ActiveImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                    @PathVariable("folderId") int folderId,
                                    @PathVariable("uuid") String uuid) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);
        ActiveImage activeImage = DBActiveImageService.getByUUID(project, folder, UUID.fromString(uuid));

        byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectId") int projectId,
                             @PathVariable("folderId") int folderId,
                             @PathVariable("uuid") String uuid) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);
        ActiveImage activeImage = DBActiveImageService.getByUUID(project, folder, UUID.fromString(uuid));
        DBActiveImageService.deleteByUUID(project, folder, activeImage);

        String fileName = activeImage.getFileName();
        MultipartFile activeImageFile = new CustomMultipartFile(fileName, localActiveImageService.getImage(project, folder, fileName));
        localActiveImageService.transfer(project, folder, activeImageFile);
    }

    @GetMapping("/get-all-by-uuid")
    public List<ActiveImageDTO> getAllByUUID(@PathVariable("projectId") int projectId,
                                             @PathVariable("folderId") int folderId,
                                             @RequestBody List<String> uuids) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(folderId);
        List<UUID> ids = uuids.stream()
                .map(UUID::fromString)
                .toList();

        List<ActiveImage> activeImages = DBActiveImageService.getAllByUUID(project, folder, ids);

        List<ActiveImageDTO> activeImageDTOS = new ArrayList<>();
        for (ActiveImage activeImage : activeImages) {
            byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
            ActiveImageDTO activeImageDTO = activeImageMapper.toDTO(activeImage, bytes);
            activeImageDTOS.add(activeImageDTO);
        }
        return activeImageDTOS;
    }
}
