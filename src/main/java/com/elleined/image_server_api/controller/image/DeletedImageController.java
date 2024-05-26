package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.CustomMultipartFile;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.ImageService;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
import com.elleined.image_server_api.service.image.deleted.DeletedImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/deleted-images")
public class DeletedImageController {
    private final ProjectService projectService;

    private final ActiveImageService activeImageService;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageService deletedImageService;
    private final DeletedImageMapper deletedImageMapper;

    @GetMapping("/get-all-by-uuid")
    public List<DeletedImageDTO> getAllByUUID(@PathVariable("projectId") int projectId,
                                              @RequestBody List<String> uuids) {

        Project project = projectService.getById(projectId);
        List<UUID> ids = uuids.stream()
                .map(UUID::fromString)
                .toList();

        return deletedImageService.getAllByUUID(ids).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }

    @PutMapping("/{uuid}/restore")
    public ActiveImageDTO restore(@PathVariable("projectId") int projectId,
                                  @PathVariable("uuid") String uuid) throws IOException {

        Project project = projectService.getById(projectId);
        DeletedImage deletedImage = deletedImageService.getByUUID(UUID.fromString(uuid));
        ActiveImage activeImage = activeImageService.restore(project, deletedImage);

        String fileName = deletedImage.getFileName();
        MultipartFile deletedImageFile = new CustomMultipartFile(fileName, deletedImageService.getImage(project, fileName));
        deletedImageService.transfer(project, deletedImageFile);

        byte[] bytes = activeImageService.getImage(project, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @GetMapping("/{uuid}")
    public DeletedImageDTO getByUUID(@PathVariable("uuid") String uuid) {
        DeletedImage deletedImage = deletedImageService.getByUUID(UUID.fromString(uuid));
        return deletedImageMapper.toDTO(deletedImage);
    }
}
