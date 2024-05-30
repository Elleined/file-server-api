package com.elleined.image_server_api.mapper.project;

import com.elleined.image_server_api.dto.project.ProjectDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProjectMapper extends CustomMapper<Project, ProjectDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "folderIds", expression = "java(project.getAllFolderIds())")
    })
    ProjectDTO toDTO(Project project);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "name", expression = "java(name)"),
            @Mapping(target = "folders", expression = "java(new java.util.ArrayList<>())")
    })
    Project toEntity(String name);
}
