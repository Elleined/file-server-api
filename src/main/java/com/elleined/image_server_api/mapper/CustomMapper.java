package com.elleined.image_server_api.mapper;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;

public interface CustomMapper<ENTITY extends PrimaryKeyIdentity,
        DTO extends com.elleined.image_server_api.dto.DTO> {
    DTO toDTO(ENTITY entity);
}