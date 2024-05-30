package com.elleined.image_server_api.dto.folder;

import com.elleined.image_server_api.dto.IntegerDTO;
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
public class FolderDTO extends IntegerDTO {
    private String name;
    private int projectId;
    private List<UUID> activeImageIds;
    private List<UUID> deletedImageIds;
}

