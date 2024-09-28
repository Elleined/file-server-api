package com.elleined.file_server_api.controller.file;

import com.elleined.file_server_api.dto.file.ActiveFileDTO;
import com.elleined.file_server_api.dto.file.DeletedFileDTO;
import com.elleined.file_server_api.mapper.file.ActiveFileMapper;
import com.elleined.file_server_api.mapper.file.DeletedFileMapper;
import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.file.CustomMultipartFile;
import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.service.folder.FolderService;
import com.elleined.file_server_api.service.file.active.db.DBActiveFileService;
import com.elleined.file_server_api.service.file.deleted.db.DBDeletedFileService;
import com.elleined.file_server_api.service.file.deleted.local.LocalDeletedFileService;
import com.elleined.file_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectName}/folders/{folderName}/deleted-images")
public class DeletedFileController {
    private final ProjectService projectService;

    private final FolderService folderService;

    private final DBActiveFileService DBActiveFileService;
    private final ActiveFileMapper activeFileMapper;

    private final LocalDeletedFileService localDeletedFileService;
    private final DBDeletedFileService DBDeletedFileService;

    @PutMapping("/{uuid}/restore")
    public ActiveFileDTO restore(@PathVariable("projectName") String projectName,
                                 @PathVariable("folderName") String folderName,
                                 @PathVariable("uuid") UUID uuid) throws IOException {

        Project project = projectService.getByName(projectName);
        Folder folder = folderService.getByName(project, folderName);
        DeletedFile deletedFile = DBDeletedFileService.getByUUID(project, folder, uuid);

        ActiveFile activeImage = DBActiveFileService.restore(project, folder, deletedFile);

        String fileName = deletedFile.getFileName();
        MultipartFile deletedFileFile = new CustomMultipartFile(fileName, localDeletedFileService.getImage(project, folder, fileName));
        byte[] bytes = localDeletedFileService.getImage(project, folder, activeImage.getFileName());
        localDeletedFileService.transfer(project, folder, deletedFileFile);
        return activeFileMapper.toDTO(activeImage, bytes);
    }
}
