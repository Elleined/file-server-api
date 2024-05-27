package com.elleined.image_server_api.controller.project;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.dto.project.ProjectDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.mapper.project.ProjectMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    private final ActiveImageService activeImageService;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageMapper deletedImageMapper;

    @PostMapping
    public ProjectDTO save(@RequestParam("name") String name) throws IOException {
        Project project = projectService.save(name);
        activeImageService.createFolders(project);
        return projectMapper.toDTO(project);
    }

    @GetMapping("/{id}")
    public ProjectDTO getById(@PathVariable("id") int id) {
        Project project = projectService.getById(id);
        return projectMapper.toDTO(project);
    }

    @GetMapping
    public List<ProjectDTO> getAll(@RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                   @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                   @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                   @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return projectService.getAll(pageable).stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    @GetMapping("/{projectId}/active-images")
    public List<ActiveImageDTO> getAllActiveImages(@PathVariable("projectId") int projectId,
                                                   @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                                   @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                                   @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                                   @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) throws IOException {
        Project project = projectService.getById(projectId);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        List<ActiveImage> activeImages = projectService.getAllActiveImages(project, pageable);

        List<ActiveImageDTO> activeImageDTOS = new ArrayList<>();
        for (ActiveImage activeImage : activeImages) {
            byte[] bytes = activeImageService.getImage(project, activeImage.getFileName());
            ActiveImageDTO activeImageDTO = activeImageMapper.toDTO(activeImage, bytes);
            activeImageDTOS.add(activeImageDTO);
        }
        return activeImageDTOS;
    }

    @GetMapping("/{projectId}/deleted-images")
    public List<DeletedImageDTO> getAllDeletedImages(@PathVariable("projectId") int projectId,
                                                     @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                                     @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                                     @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                                     @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) {

        Project project = projectService.getById(projectId);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return projectService.getAllDeletedImages(project, pageable).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }
}
