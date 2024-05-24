package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.dto.DTO;
import com.elleined.image_server_api.model.image.ImageHistory;
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
public class ActiveImageDTO extends DTO {
    private int projectId;
    private List<Integer> imageHistoriesIds;
}
