package com.elleined.image_server_api.mapper.folder;

import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.project.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FolderMapper extends CustomMapper<Folder, FolderDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "projectId", source = "project.id"),
            @Mapping(target = "activeImageIds", expression = "java(folder.getAllActiveImageIds())"),
            @Mapping(target = "deletedImageIds", expression = "java(folder.getAllDeletedImageIds())"),
    })
    FolderDTO toDTO(Folder folder);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "name", expression = "java(name)"),
            @Mapping(target = "project", expression = "java(project)"),
            @Mapping(target = "activeImages", expression = "java(new java.util.ArrayList<>())"),
            @Mapping(target = "deletedImages", expression = "java(new java.util.ArrayList<>())"),
    })
    Folder toEntity(String name,
                    @Context Project project);
}
