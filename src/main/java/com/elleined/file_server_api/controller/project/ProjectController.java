package com.elleined.file_server_api.controller.project;

import com.elleined.file_server_api.dto.project.ProjectDTO;
import com.elleined.file_server_api.mapper.project.ProjectMapper;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
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
    public ProjectDTO save(@RequestParam("name") String name,
                           @RequestParam("folderNames") List<String> folderNames) throws IOException {

        Project project = projectService.save(name);
        folderService.saveAll(project, folderNames);

        folderService.createFolder(project);
        return projectMapper.toDTO(project);
    }

    @GetMapping("/{name}")
    public ProjectDTO getByName(@PathVariable("name") String name) {
        Project project = projectService.getByName(name);
        return projectMapper.toDTO(project);
    }
}
