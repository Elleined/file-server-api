package com.elleined.image_server_api.populator;

import com.elleined.image_server_api.model.project.Project;
import com.elleined.image_server_api.service.image.active.ActiveImageService;
import com.elleined.image_server_api.service.project.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Transactional
public class ProjectPopulator extends Populator {
    private final ProjectService projectService;
    private final ActiveImageService activeImageService;

    public ProjectPopulator(ObjectMapper objectMapper, ProjectService projectService, ActiveImageService activeImageService) {
        super(objectMapper);
        this.projectService = projectService;
        this.activeImageService = activeImageService;
    }

    @Override
    public void populate(String jsonFile) throws IOException {
        var resource = new ClassPathResource(jsonFile);
        byte[] dataBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        var type = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);

        List<String> projects = objectMapper.readValue(new String(dataBytes, StandardCharsets.UTF_8), type);
        List<Project> savedProjects = projectService.saveAll(projects);
        for (Project savedProject : savedProjects) {
            activeImageService.createFolders(savedProject);
        }
    }
}
