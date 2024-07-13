package com.elleined.image_server_api.dto.project;

import com.elleined.image_server_api.dto.IntegerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectDTO extends IntegerDTO {
    private String name;
}
