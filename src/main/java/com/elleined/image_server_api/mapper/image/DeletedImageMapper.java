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
            @Mapping(target = "lastAccessedAt", source = "lastAccessedAt"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "formatId", source = "format.id"),
            @Mapping(target = "fileName", source = "fileName"),
            @Mapping(target = "fileSizeInMB", source = "fileSizeInMB"),
            @Mapping(target = "folderId", source = "folder.id")
    })
    DeletedImageDTO toDTO(DeletedImage deletedImage);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "format", source = "format"),
            @Mapping(target = "fileName", source = "fileName"),
            @Mapping(target = "fileSizeInMB", source = "fileSizeInMB"),
            @Mapping(target = "folder", source = "folder")
    })
    DeletedImage toEntity(ActiveImage activeImage);
}
