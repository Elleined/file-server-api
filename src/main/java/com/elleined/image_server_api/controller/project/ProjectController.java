package com.elleined.image_server_api.controller.project;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.dto.project.ProjectDTO;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.mapper.project.ProjectMapper;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    private final ActiveImageMapper activeImageMapper;
    private final DeletedImageMapper deletedImageMapper;

    @PostMapping
    public ProjectDTO save(@RequestParam("name") String name) {
        Project project = projectService.save(name);
        return projectMapper.toDTO(project);
    }

    @GetMapping("/{id}")
    public ProjectDTO getById(@PathVariable("id") int id) {
        Project project = projectService.getById(id);
        return projectMapper.toDTO(project);
    }

    @GetMapping
    public List<ProjectDTO> getAll() {
        return projectService.getAll().stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    @GetMapping("/{projectId}/active-images")
    public List<ActiveImageDTO> getAllActiveImages(@PathVariable("projectId") int projectId) {
        Project project = projectService.getById(projectId);
        return projectService.getAllActiveImages(project).stream()
                .map(activeImageMapper::toDTO)
                .toList();
    }

    @GetMapping("/{projectId}/deleted-images")
    public List<DeletedImageDTO> getAllDeletedImages(@PathVariable("projectId") int projectId) {
        Project project = projectService.getById(projectId);
        return projectService.getAllDeletedImages(project).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }
}
