package com.elleined.image_server_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class UUIDDTO extends DTO {
    private UUID id;

    public UUIDDTO(LocalDateTime createdAt, UUID id) {
        super(createdAt);
        this.id = id;
    }
}
