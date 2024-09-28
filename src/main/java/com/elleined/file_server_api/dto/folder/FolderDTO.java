package com.elleined.file_server_api.dto.folder;

import com.elleined.file_server_api.dto.IntegerDTO;
import com.elleined.file_server_api.dto.project.ProjectDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FolderDTO extends IntegerDTO {
    private String name;
    private ProjectDTO projectDTO;
}

