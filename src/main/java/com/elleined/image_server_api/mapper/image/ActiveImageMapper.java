package com.elleined.image_server_api.mapper.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.model.project.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ActiveImageMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "lastAccessedAt", source = "lastAccessedAt"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "imageFormatId", source = "imageFormat.id"),
            @Mapping(target = "bytes", expression = "java(bytes)"),
            @Mapping(target = "projectId", source = "project.id")
    })
    ActiveImageDTO toDTO(ActiveImage activeImage,
                         @Context byte[] bytes);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", expression = "java(description)"),
            @Mapping(target = "additionalInformation", expression = "java(additionalInformation)"),
            @Mapping(target = "imageFormat", expression = "java(imageFormat)"),
            @Mapping(target = "fileName", expression = "java(fileName)"),
            @Mapping(target = "project", expression = "java(project)")
    })
    ActiveImage toEntity(String description,
                         String additionalInformation,
                         ImageFormat imageFormat,
                         String fileName,
                         Project project);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "imageFormat", source = "imageFormat"),
            @Mapping(target = "fileName", source = "fileName"),
            @Mapping(target = "project", source = "project")
    })
    ActiveImage toEntity(DeletedImage deletedImage); // For restoring deleted images;
}
