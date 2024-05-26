package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.ImageService;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/active-images")
public class ActiveImageController {
    private final ProjectService projectService;

    private final ActiveImageService activeImageService;
    private final ActiveImageMapper activeImageMapper;

    private final ImageService imageService;

    @PostMapping
    public ActiveImageDTO save(@PathVariable("projectId") int projectId,
                               @RequestPart("image") MultipartFile image,
                               @RequestPart("description") String description,
                               @RequestPart("additionalInformation") String additionalInformation) throws IOException {

        Project project = projectService.getById(projectId);
        ActiveImage activeImage = activeImageService.save(project, description, additionalInformation, image);

        byte[] bytes = imageService.getImage(project, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @GetMapping("/{uuid}")
    public ActiveImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                    @PathVariable("uuid") String uuid) {

        Project project = projectService.getById(projectId);
        ActiveImage activeImage = activeImageService.getByUUID(project, UUID.fromString(uuid));

        byte[] bytes = imageService.getImage(project, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectId") int projectId,
                             @PathVariable("uuid") String uuid) {

        Project project = projectService.getById(projectId);
        activeImageService.deleteByUUID(project, UUID.fromString(uuid));
    }

    @GetMapping("/get-all-by-uuid")
    public List<ActiveImageDTO> getAllByUUID(@PathVariable("projectId") int projectId,
                                             @RequestBody List<String> uuids) {

        Project project = projectService.getById(projectId);
        List<UUID> ids = uuids.stream()
                .map(UUID::fromString)
                .toList();

        return activeImageService.getAllByUUID(ids).stream()
                .map(activeImage -> {
                    byte[] bytes = imageService.getImage(project, activeImage.getFileName());
                    return activeImageMapper.toDTO(activeImage, bytes);
                })
                .toList();
    }
}
