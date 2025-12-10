package com.elleined.file_server_api.folder;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public String save() throws IOException {
        return folderService.save().toString();
    }

    @DeleteMapping("/{folder}")
    public void delete(@PathVariable("folder") UUID folder) throws IOException {
        folderService.delete(folder);
    }
}
