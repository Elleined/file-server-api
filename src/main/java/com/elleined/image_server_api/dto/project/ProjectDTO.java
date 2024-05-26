package com.elleined.image_server_api.dto.project;

import com.elleined.image_server_api.dto.DTO;
import com.elleined.image_server_api.dto.IntegerDTO;
import com.elleined.image_server_api.dto.UUIDDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectDTO extends IntegerDTO {
    private String name;
    private List<UUID> activeImageIds;
    private List<UUID> deletedImageIds;
}
