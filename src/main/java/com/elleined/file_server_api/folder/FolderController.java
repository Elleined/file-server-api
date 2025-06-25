package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/{folder}")
    public String save(@PathVariable("folder") String folder) {
        return folderService.save(folder)
                .getFileName()
                .toString();
    }
}
