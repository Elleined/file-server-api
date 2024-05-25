package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.model.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DeletedImageDTO extends ImageDTO {
    private int projectId;
}
