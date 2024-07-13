package com.elleined.image_server_api.mapper.folder;

import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.mapper.project.ProjectMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.project.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = ProjectMapper.class
)
public interface FolderMapper extends CustomMapper<Folder, FolderDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "projectDTO", source = "project")
    })
    FolderDTO toDTO(Folder folder);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "project", source = "project"),
            @Mapping(target = "activeImages", expression = "java(new java.util.ArrayList<>())"),
            @Mapping(target = "deletedImages", expression = "java(new java.util.ArrayList<>())"),
    })
    Folder toEntity(String name,
                    Project project);
}
