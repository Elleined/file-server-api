package com.elleined.file_server_api.controller.image;

import com.elleined.file_server_api.dto.image.ActiveImageDTO;
import com.elleined.file_server_api.dto.image.DeletedImageDTO;
import com.elleined.file_server_api.mapper.image.ActiveImageMapper;
import com.elleined.file_server_api.mapper.image.DeletedImageMapper;
import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.CustomMultipartFile;
import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.image.active.db.DBActiveImageService;
import com.elleined.file_server_api.service.image.deleted.db.DBDeletedImageService;
import com.elleined.file_server_api.service.image.deleted.local.LocalDeletedImageService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders/{folderName}/deleted-images")
public class DeletedImageController {
    private final ProjectService projectService;

    private final FolderService folderService;

    private final DBActiveImageService DBActiveImageService;
    private final ActiveImageMapper activeImageMapper;

    private final LocalDeletedImageService localDeletedImageService;
    private final DBDeletedImageService DBDeletedImageService;
    private final DeletedImageMapper deletedImageMapper;

    @GetMapping("/{uuid}")
    public DeletedImageDTO getByUUID(@PathVariable("projectName") String projectName,
                                     @PathVariable("folderName") String folderName,
                                     @PathVariable("uuid") UUID uuid) {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);
        DeletedFile deletedImage = DBDeletedImageService.getByUUID(project, folder, uuid);
        return deletedImageMapper.toDTO(deletedImage);
    }

    @PutMapping("/{uuid}/restore")
    public ActiveImageDTO restore(@PathVariable("projectName") String projectName,
                                  @PathVariable("folderName") String folderName,
                                  @PathVariable("uuid") UUID uuid) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);
        DeletedFile deletedImage = DBDeletedImageService.getByUUID(project, folder, uuid);

        ActiveFile activeImage = DBActiveImageService.restore(project, folder, deletedImage);

        String fileName = deletedImage.getFileName();
        MultipartFile deletedImageFile = new CustomMultipartFile(fileName, localDeletedImageService.getImage(project, folder, fileName));
        localDeletedImageService.transfer(project, folder, deletedImageFile);

        byte[] bytes = localDeletedImageService.getImage(project, folder, activeImage.getFileName());
        return activeImageMapper.toDTO(activeImage, bytes);
    }
}
