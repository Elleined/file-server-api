package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
import com.elleined.image_server_api.service.image.deleted.DeletedImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/deleted-images")
public class DeletedImageController {
    private final ProjectService projectService;

    private final ActiveImageService activeImageService;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageService deletedImageService;
    private final DeletedImageMapper deletedImageMapper;

    @GetMapping("/get-all-by-id")
    public List<DeletedImageDTO> getAllById(List<Integer> ids) {
        return deletedImageService.getAllById(ids).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }

    @PutMapping("/{uuid}/restore")
    public ActiveImageDTO restore(@PathVariable("projectId") int projectId,
                                  @PathVariable("uuid") String uuid) {

        Project project = projectService.getById(projectId);
        DeletedImage deletedImage = deletedImageService.getByUUID(uuid);
        ActiveImage activeImage = activeImageService.restore(project, deletedImage);

        return activeImageMapper.toDTO(activeImage);
    }

    @GetMapping("/{uuid}")
    public DeletedImageDTO getByUUID(@PathVariable("uuid") String uuid) {
        DeletedImage deletedImage = deletedImageService.getByUUID(uuid);
        return deletedImageMapper.toDTO(deletedImage);
    }
}
