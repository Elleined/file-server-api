package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.request.ImageRequest;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
                               @Valid @RequestBody ImageRequest imageRequest) throws IOException {

        Project project = projectService.getById(projectId);
        ActiveImage activeImage = activeImageService.save(project, image, imageRequest);
        return activeImageMapper.toDTO(activeImage);
    }

    @GetMapping("/{uuid}")
    public ActiveImageDTO getByUUID(@PathVariable("projectId") int projectId,
                                    @PathVariable("uuid") String uuid) {

        Project project = projectService.getById(projectId);
        ActiveImage activeImage = activeImageService.getByUUID(project, uuid);

        return activeImageMapper.toDTO(activeImage);
    }

    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectId") int projectId,
                             @PathVariable("uuid") String uuid) {

        Project project = projectService.getById(projectId);
        activeImageService.deleteByUUID(project, uuid);
    }

    @GetMapping("/get-all-by-id")
    public List<ActiveImageDTO> getAllById(List<Integer> ids) {
        return activeImageService.getAllById(ids).stream()
                .map(activeImageMapper::toDTO)
                .toList();
    }
}
