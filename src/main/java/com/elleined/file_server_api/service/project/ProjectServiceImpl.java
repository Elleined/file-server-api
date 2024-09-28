package com.elleined.file_server_api.service.project;

import com.elleined.file_server_api.exception.resource.ResourceAlreadyExistsException;
import com.elleined.file_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.file_server_api.mapper.project.ProjectMapper;
import com.elleined.file_server_api.model.project.Project;
import com.elleined.file_server_api.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public Project save(String name) {
        if (isNameAlreadyExists(name))
            throw new ResourceAlreadyExistsException("Cannot save project! Because project name " + name + " already exists");
        Project project = projectMapper.toEntity(name);

        projectRepository.save(project);
        log.debug("Saving project with name of {} success", name);
        return project;
    }

    @Override
    public Project getById(int id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with id of " + id + " does not exists!"));
    }

    @Override
    public Project getByName(String name) {
        return projectRepository.findByName(name);
    }

    @Override
    public Page<Project> getAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public boolean isNameAlreadyExists(String name) {
        return projectRepository.findAll().stream()
                .map(Project::getName)
                .anyMatch(name::equalsIgnoreCase);
    }
}
