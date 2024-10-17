package com.elleined.file_server_api.controller.file;

import com.elleined.file_server_api.service.file.deleted.DeletedFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders/{folderName}/deleted-images")
public class DeletedFileController {
    private final DeletedFileService deletedFileService;

    @PutMapping("/{fileName}/restore")
    public String restore(@PathVariable("projectName") String projectName,
                          @PathVariable("folderName") String folderName,
                          @PathVariable("fileName") String fileName) throws IOException {

        deletedFileService.restore(projectName, folderName, fileName);
        return fileName;
    }
}
