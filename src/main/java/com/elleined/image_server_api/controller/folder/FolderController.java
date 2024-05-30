package com.elleined.image_server_api.controller.folder;


import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.mapper.folder.FolderMapper;
import com.elleined.image_server_api.mapper.image.ActiveImageMapper;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.folder.FolderService;
import com.elleined.image_server_api.service.image.active.local.LocalActiveImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/folders")
public class FolderController {
    private final ProjectService projectService;

    private final FolderService folderService;
    private final FolderMapper folderMapper;

    private final LocalActiveImageService localActiveImageService;
    private final ActiveImageMapper activeImageMapper;

    private final DeletedImageMapper deletedImageMapper;

    @PostMapping
    public FolderDTO save(@PathVariable("projectId") int projectId,
                          @RequestParam("folderName") String name) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.save(project, name);

        folderService.createFolder(project, folder);
        return folderMapper.toDTO(folder);
    }

    @GetMapping("/{folderId}")
    public FolderDTO getById(@PathVariable("projectId") int projectId,
                             @PathVariable("folderId") int folderId) {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);

        return folderMapper.toDTO(folder);
    }

    @GetMapping
    public List<FolderDTO> getAll(@PathVariable("projectId") int projectId,
                                  @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                  @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                  @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                  @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) {


        Project project = projectService.getById(projectId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return folderService.getAll(project, pageable).stream()
                .map(folderMapper::toDTO)
                .toList();
    }

    @GetMapping("/{folderId}/active-images")
    public List<ActiveImageDTO> getAllActiveImages(@PathVariable("projectId") int projectId,
                                                   @PathVariable("folderId") int folderId,
                                                   @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                                   @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                                   @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                                   @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) throws IOException {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);

        List<ActiveImage> activeImages = folderService.getAllActiveImages(project, folder, pageable);
        List<ActiveImageDTO> activeImageDTOS = new ArrayList<>();
        for (ActiveImage activeImage : activeImages) {
            byte[] bytes = localActiveImageService.getImage(project, folder, activeImage.getFileName());
            ActiveImageDTO activeImageDTO = activeImageMapper.toDTO(activeImage, bytes);
            activeImageDTOS.add(activeImageDTO);
        }
        return activeImageDTOS;
    }

    @GetMapping("/{folderId}/deleted-images")
    public List<DeletedImageDTO> getAllDeletedImages(@PathVariable("projectId") int projectId,
                                                     @PathVariable("folderId") int folderId,
                                                     @RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                                     @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                                     @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                                     @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) {

        Project project = projectService.getById(projectId);
        Folder folder = folderService.getById(project, folderId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return folderService.getAllDeletedImages(project, folder, pageable).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }

    @GetMapping("/get-all-by-id")
    public List<FolderDTO> getAllById(@PathVariable("projectId") int projectId,
                                      @RequestBody List<Integer> ids) {

        Project project = projectService.getById(projectId);

        return folderService.getAllById(project, ids).stream()
                .map(folderMapper::toDTO)
                .toList();
    }
}
