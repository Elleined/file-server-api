package com.elleined.image_server_api.mapper.image;

import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DeletedImageMapper extends CustomMapper<DeletedImage, DeletedImageDTO> {

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
            @Mapping(target = "projectId", source = "project.id"),
            @Mapping(target = "imageHistoriesIds", expression = "java(new java.util.ArrayList<>())")
    })
    DeletedImageDTO toDTO(DeletedImage deletedImage);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "lastAccessedAt", source = "lastAccessedAt"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "imageFormat", source = "imageFormat"),
            @Mapping(target = "image", source = "image"),
            @Mapping(target = "project", source = "project"),
            @Mapping(target = "imageHistories", expression = "java(new java.util.ArrayList<>())")
    })
    DeletedImage toEntity(ActiveImage activeImage);
}
