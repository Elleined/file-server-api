package com.elleined.image_server_api.mapper;

public interface CustomMapper<ENTITY,
        DTO extends com.elleined.image_server_api.dto.DTO> {
    DTO toDTO(ENTITY entity);
}