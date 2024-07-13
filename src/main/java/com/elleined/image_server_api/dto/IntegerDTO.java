package com.elleined.image_server_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class IntegerDTO extends DTO {
    private int id;

    public IntegerDTO(LocalDateTime createdAt, int id) {
        super(createdAt);
        this.id = id;
    }
}
