package com.elleined.image_server_api.mapper.image;

import com.elleined.image_server_api.dto.image.ActiveImageDTO;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.format.Format;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
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
            @Mapping(target = "formatId", source = "format.id"),
            @Mapping(target = "fileName", source = "fileName"),
            @Mapping(target = "folderId", source = "folder.id"),
            @Mapping(target = "bytes", expression = "java(bytes)")
    })
    ActiveImageDTO toDTO(ActiveImage activeImage,
                         @Context byte[] bytes);

    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", expression = "java(description)"),
            @Mapping(target = "additionalInformation", expression = "java(additionalInformation)"),
            @Mapping(target = "format", expression = "java(format)"),
            @Mapping(target = "fileName", expression = "java(fileName)"),
            @Mapping(target = "folder", expression = "java(folder)")
    })
    ActiveImage toEntity(String description,
                         String additionalInformation,
                         Format format,
                         String fileName,
                         Folder folder);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "lastAccessedAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "format", source = "format"),
            @Mapping(target = "fileName", source = "fileName"),
            @Mapping(target = "folder", source = "folder")
    })
    ActiveImage toEntity(DeletedImage deletedImage); // For restoring deleted images;
}
