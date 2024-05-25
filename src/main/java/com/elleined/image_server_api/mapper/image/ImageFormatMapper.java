package com.elleined.image_server_api.mapper.image;

import com.elleined.image_server_api.dto.image.ImageFormatDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.image.ImageFormat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ImageFormatMapper extends CustomMapper<ImageFormat, ImageFormatDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "format", source = "format")
    })
    ImageFormatDTO toDTO(ImageFormat imageFormat);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "format", expression = "java(format)")
    })
    ImageFormat toEntity(String format);
}
