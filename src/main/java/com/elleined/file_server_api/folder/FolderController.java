package com.elleined.file_server_api.folder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/{folder}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void save(@PathVariable("folder") String folder) {
        folderService.save(folder);
    }

    @DeleteMapping("/{folder}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("folder") String folder) {
        folderService.delete(folder);
    }
}
