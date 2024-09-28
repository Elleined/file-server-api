package com.elleined.file_server_api.controller.file;

import com.elleined.file_server_api.dto.file.ActiveFileDTO;
import com.elleined.file_server_api.mapper.file.ActiveFileMapper;
import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.CustomMultipartFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.file.active.db.DBActiveFileService;
import com.elleined.file_server_api.service.file.active.local.LocalActiveFileService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders/{folderName}/active-images")
public class ActiveFileController {
    private final ProjectService projectService;

    private final FolderService folderService;

    private final LocalActiveFileService localActiveFileService;
    private final DBActiveFileService DBActiveFileService;
    private final ActiveFileMapper activeFileMapper;

    @PostMapping
    public ActiveFileDTO save(@PathVariable("projectName") String projectName,
                              @PathVariable("folderName") String folderName,
                              @RequestPart("file") MultipartFile file,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "additionalInformation", required = false) String additionalInformation) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);

        ActiveFile activeImage = DBActiveFileService.save(project, folder, description, additionalInformation, file);

        byte[] bytes = localActiveFileService.getImage(project, folder, activeImage.getFileName());
        return activeFileMapper.toDTO(activeImage, bytes);
    }


    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectName") String projectName,
                             @PathVariable("folderName") String folderName,
                             @PathVariable("uuid") UUID uuid) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);
        ActiveFile activeImage = DBActiveFileService.getByUUID(project, folder, uuid);
        DBActiveFileService.deleteByUUID(project, folder, activeImage);

        String fileName = activeImage.getFileName();
        MultipartFile activeImageFile = new CustomMultipartFile(fileName, localActiveFileService.getImage(project, folder, fileName));
        localActiveFileService.transfer(project, folder, activeImageFile);
    }
}
