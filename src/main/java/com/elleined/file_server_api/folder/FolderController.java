package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/{folderName}")
    public String save(@PathVariable("projectName") String projectName,
                       @PathVariable("folderName") String name) throws IOException {

        folderService.createProjectFolderDirectory(projectName, name.strip());
        return name;
    }
}
