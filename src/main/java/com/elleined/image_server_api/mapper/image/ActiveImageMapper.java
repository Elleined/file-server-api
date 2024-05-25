package com.elleined.image_server_api.mapper.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.model.project.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ActiveImageMapper extends CustomMapper<ActiveImage, ActiveImageDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "lastAccessedAt", source = "lastAccessedAt"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "imageFormatId", source = "imageFormat.id"),
            @Mapping(target = "bytes", source = "bytes"),
            @Mapping(target = "projectId", source = "project.id")
    })
    ActiveImageDTO toDTO(ActiveImage activeImage);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString())"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", expression = "java(description)"),
            @Mapping(target = "additionalInformation", expression = "java(additionalInformation)"),
            @Mapping(target = "imageFormat", expression = "java(imageFormat)"),
            @Mapping(target = "bytes", expression = "java(bytes)"),
            @Mapping(target = "project", expression = "java(project)")
    })
    ActiveImage toEntity(String description,
                         String additionalInformation,
                         ImageFormat imageFormat,
                         @Context byte[] bytes,
                         Project project);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "imageFormat", source = "imageFormat"),
            @Mapping(target = "bytes", source = "bytes"),
            @Mapping(target = "project", source = "project")
    })
    ActiveImage toEntity(DeletedImage deletedImage); // For restoring deleted images;
}
