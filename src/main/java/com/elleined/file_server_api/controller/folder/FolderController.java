package com.elleined.file_server_api.controller.folder;

import com.elleined.file_server_api.dto.folder.FolderDTO;
import com.elleined.file_server_api.mapper.folder.FolderMapper;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders")
public class FolderController {
    private final ProjectService projectService;

    private final FolderService folderService;
    private final FolderMapper folderMapper;

    @PostMapping
    public FolderDTO save(@PathVariable("projectName") String projectName,
                          @RequestParam("folderName") String name) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.save(project, name);

        folderService.createFolder(project, folder);

        return folderMapper.toDTO(folder);
    }

    @GetMapping
    public List<FolderDTO> getAll(@PathVariable("projectName") String projectName) {
        Project project = projectService.getByName(projectName);
        return folderService.getAll(project).stream()
                .map(folderMapper::toDTO)
                .toList();
    }
}
