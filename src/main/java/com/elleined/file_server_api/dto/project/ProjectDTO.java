package com.elleined.file_server_api.dto.project;

import com.elleined.file_server_api.dto.IntegerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectDTO extends IntegerDTO {
    private String name;
}
