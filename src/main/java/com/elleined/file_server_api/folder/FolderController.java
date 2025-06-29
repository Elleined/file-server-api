package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public String save() throws IOException {
        return folderService.save();
    }

    @DeleteMapping("/{folder}")
    public void deleteByName(@PathVariable("folder") String folder) throws IOException {
        folderService.deleteByName(folder);
    }
}
