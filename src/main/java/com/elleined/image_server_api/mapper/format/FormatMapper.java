package com.elleined.image_server_api.mapper.format;

import com.elleined.image_server_api.dto.format.FormatDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.format.Format;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FormatMapper extends CustomMapper<Format, FormatDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "format", source = "format")
    })
    FormatDTO toDTO(Format format);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "format", source = "format")
    })
    Format toEntity(String format);
}
