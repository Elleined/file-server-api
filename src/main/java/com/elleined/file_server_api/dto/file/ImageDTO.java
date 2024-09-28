package com.elleined.file_server_api.dto.file;

import com.elleined.file_server_api.dto.UUIDDTO;
import com.elleined.file_server_api.dto.folder.FolderDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class ImageDTO extends UUIDDTO {
    private LocalDateTime lastAccessedAt;
    private String description;
    private String additionalInformation;
    private String fileName;
    private FolderDTO folderDTO;
    private double fileSizeInMB;

}
