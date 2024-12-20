package com.elleined.file_server_api.controller.folder;

import com.elleined.file_server_api.service.folder.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public String save(@PathVariable("projectName") String projectName,
                       @RequestParam("folderName") String name) throws IOException {

        folderService.createProjectFolderDirectory(projectName, name.strip());
        return name;
    }
}
