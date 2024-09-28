package com.elleined.file_server_api.controller.image;

import com.elleined.file_server_api.dto.image.ActiveImageDTO;
import com.elleined.file_server_api.mapper.image.ActiveImageMapper;
import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.CustomMultipartFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.image.active.db.DBActiveImageService;
import com.elleined.file_server_api.service.image.active.local.LocalActiveImageService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders/{folderName}/active-images")
public class ActiveImageController {
    private final ProjectService projectService;

    private final FolderService folderService;

    private final LocalActiveImageService localActiveImageService;
    private final DBActiveImageService DBActiveImageService;
    private final ActiveImageMapper activeImageMapper;

    @GetMapping("/{uuid}")
    public ActiveImageDTO getByUUID(@PathVariable("projectName") String projectName,
                                    @PathVariable("folderName") String folderName,
                                    @PathVariable("uuid") UUID uuid) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);
        ActiveFile activeImage = DBActiveImageService.getByUUID(project, folder, uuid);

        byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }

    @PostMapping
    public ActiveImageDTO save(@PathVariable("projectName") String projectName,
                               @PathVariable("folderName") String folderName,
                               @RequestPart("image") MultipartFile image,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "additionalInformation", required = false) String additionalInformation) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);

        ActiveFile activeImage = DBActiveImageService.save(project, folder, description, additionalInformation, image);

        byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }


    @DeleteMapping("/{uuid}")
    public void deleteByUUID(@PathVariable("projectName") String projectName,
                             @PathVariable("folderName") String folderName,
                             @PathVariable("uuid") UUID uuid) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);
        ActiveFile activeImage = DBActiveImageService.getByUUID(project, folder, uuid);
        DBActiveImageService.deleteByUUID(project, folder, activeImage);

        String fileName = activeImage.getFileName();
        MultipartFile activeImageFile = new CustomMultipartFile(fileName, localActiveImageService.getImage(project, folder, fileName));
        localActiveImageService.transfer(project, folder, activeImageFile);
    }
}
