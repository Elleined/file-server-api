package com.elleined.file_server_api.controller.project;

import com.elleined.file_server_api.service.folder.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final FolderService folderService;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @PostMapping
    public String save(@RequestParam("name") String name,
                       @RequestParam("folderNames") List<String> folderNames) throws IOException {

        folderService.createProjectDirectory(name);
        folderNames.forEach(folderName -> {
            try {
                folderService.createProjectFolderDirectory(name, folderName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return name;
    }
}
