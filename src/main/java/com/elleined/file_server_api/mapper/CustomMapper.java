package com.elleined.file_server_api.mapper;

public interface CustomMapper<ENTITY,
        DTO extends com.elleined.file_server_api.dto.DTO> {
    DTO toDTO(ENTITY entity);
}