package com.elleined.image_server_api.service.folder;

import com.elleined.image_server_api.exception.resource.ResourceAlreadyExistsException;
import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.exception.resource.ResourceNotOwnedException;
import com.elleined.image_server_api.mapper.folder.FolderMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.repository.FolderRepository;
import com.elleined.image_server_api.repository.project.ProjectRepository;
import com.elleined.image_server_api.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    private final FolderRepository folderRepository;
    private final FolderMapper folderMapper;

    @Override
    public Folder save(Project project, String name) {
        if (isNameAlreadyExists(name))
            throw new ResourceAlreadyExistsException("Cannot save folder! because " + name + " already exists");

        Folder folder = folderMapper.toEntity(name, project);
        project.getFolders().add(folder);

        folderRepository.save(folder);
        projectRepository.save(project);
        log.debug("Saving folder success");
        return folder;
    }

    @Override
    public Folder getById(Project project, int id) throws ResourceNotFoundException {
        if (!project.has(id))
            throw new ResourceNotOwnedException("Cannot get by id! because this project doesn't have a folder with id of " + id);

        return folderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Folder with id of " + id + " doesn't exists!"));
    }

    @Override
    public List<Folder> getAll(Project project, Pageable pageable) {
        return projectRepository.findAllFolders(project, pageable).getContent();
    }

    @Override
    public List<Folder> getAllById(Project project, List<Integer> ids) {
        return folderRepository.findAllById(ids).stream()
                .sorted(Comparator.comparing(Folder::getName))
                .toList();
    }

    @Override
    public boolean isNameAlreadyExists(String name) {
        return folderRepository.findAll().stream()
                .map(Folder::getName)
                .anyMatch(name::equalsIgnoreCase);
    }


    @Override
    public List<ActiveImage> getAllActiveImages(Project project, Folder folder, Pageable pageable) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get all active images! because this project doesn't have this folder!");

        return folderRepository.findAllActiveImages(folder, pageable).stream().toList();
    }

    @Override
    public List<DeletedImage> getAllDeletedImages(Project project, Folder folder, Pageable pageable) {
        if (!projectService.has(project, folder))
            throw new ResourceNotOwnedException("Cannot get all deleted images! because this project doesn't have this folder!");

        return folderRepository.findAllDeletedImages(folder, pageable).stream().toList();
    }

    @Override
    public List<Folder> saveAll(Project project, List<String> names) {
        if (names.stream().anyMatch(this::isNameAlreadyExists))
            throw new ResourceAlreadyExistsException("Cannot save project! because one of the folder names already exists!");

        return names.stream()
                .map(name -> save(project, name))
                .toList();
    }
}
