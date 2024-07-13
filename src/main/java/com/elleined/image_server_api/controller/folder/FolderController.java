package com.elleined.image_server_api.controller.folder;

import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.mapper.folder.FolderMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.folder.FolderService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/folders")
public class FolderController {
    private final ProjectService projectService;

    private final FolderService folderService;
    private final FolderMapper folderMapper;

    @PostMapping
    public FolderDTO save(@PathVariable("projectId") int projectId,
                          @RequestParam("folderName") String name,
                          @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.save(project, name);

        folderService.createFolder(project, folder);
        return folderMapper.toDTO(folder);
    }

    @GetMapping
    public Page<FolderDTO> getAll(@PathVariable("projectId") int projectId,
                                  @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                  @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                  @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                  @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy,
                                  @RequestParam(defaultValue = "false", name = "includeRelatedLinks") boolean includeRelatedLinks) {


        Project project = projectService.getById(projectId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return folderService.getAll(project, pageable)
                .map(folderMapper::toDTO);
    }
}
