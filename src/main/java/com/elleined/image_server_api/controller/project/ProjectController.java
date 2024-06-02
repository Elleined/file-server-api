package com.elleined.image_server_api.controller.project;

import com.elleined.image_server_api.dto.project.ProjectDTO;
import com.elleined.image_server_api.mapper.project.ProjectMapper;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.folder.FolderService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    private final FolderService folderService;

    @PostMapping
    public ProjectDTO save(@RequestPart("name") String name,
                           @RequestPart("folderNames") List<String> folderNames) throws IOException {

        Project project = projectService.save(name);
        folderService.saveAll(project, folderNames);

        folderService.createFolder(project);
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
}
