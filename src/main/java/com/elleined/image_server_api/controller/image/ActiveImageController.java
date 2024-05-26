package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.exception.image.ImageException;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.CustomMultipartFile;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
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
@RequestMapping("/projects/{projectId}/active-images")
public class ActiveImageController {
    private final ProjectService projectService;

    private final ActiveImageService activeImageService;
    private final ActiveImageMapper activeImageMapper;

    @PostMapping
    public ActiveImageDTO save(@PathVariable("projectId") int projectId,
                               @RequestPart("image") MultipartFile image,
                               @RequestPart(value = "description", required = false) String description,
                               @RequestPart(value = "additionalInformation", required = false) String additionalInformation) throws IOException {

        Project project = projectService.getById(projectId);

        ActiveImage activeImage = activeImageService.save(project, description, additionalInformation, image);
        activeImageService.save(project, image);

        byte[] bytes = activeImageService.getImage(project, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @GetMapping("/{uuid}")
    public ActiveImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                    @PathVariable("uuid") String uuid) throws IOException {

        Project project = projectService.getById(projectId);
        ActiveImage activeImage = activeImageService.getByUUID(project, UUID.fromString(uuid));

        byte[] bytes = activeImageService.getImage(project, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectId") int projectId,
                             @PathVariable("uuid") String uuid) throws IOException {

        Project project = projectService.getById(projectId);
        ActiveImage activeImage = activeImageService.getByUUID(project, UUID.fromString(uuid));
        activeImageService.deleteByUUID(project, activeImage);

        String fileName = activeImage.getFileName();
        MultipartFile activeImageFile = new CustomMultipartFile(fileName, activeImageService.getImage(project, fileName));
        activeImageService.transfer(project, activeImageFile);
    }

    @GetMapping("/get-all-by-uuid")
    public List<ActiveImageDTO> getAllByUUID(@PathVariable("projectId") int projectId,
                                             @RequestBody List<String> uuids) throws IOException {

        Project project = projectService.getById(projectId);
        List<UUID> ids = uuids.stream()
                .map(UUID::fromString)
                .toList();

        List<ActiveImage> activeImages = activeImageService.getAllByUUID(project, ids);

        List<ActiveImageDTO> activeImageDTOS = new ArrayList<>();
        for (ActiveImage activeImage : activeImages) {
            byte[] bytes = activeImageService.getImage(project, activeImage.getFileName());
            ActiveImageDTO activeImageDTO = activeImageMapper.toDTO(activeImage, bytes);
            activeImageDTOS.add(activeImageDTO);
        }
        return activeImageDTOS;
    }
}
