package com.elleined.image_server_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class DTO extends HateoasDTO {
    private LocalDateTime createdAt;

    public DTO(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
