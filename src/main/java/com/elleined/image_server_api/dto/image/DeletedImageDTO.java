package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.dto.format.FormatDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DeletedImageDTO extends ImageDTO {

    @Builder
    public DeletedImageDTO(LocalDateTime createdAt, UUID id, LocalDateTime lastAccessedAt, String description, String additionalInformation, FormatDTO formatDTO, String fileName, FolderDTO folderDTO, double fileSizeInMB) {
        super(createdAt, id, lastAccessedAt, description, additionalInformation, formatDTO, fileName, folderDTO, fileSizeInMB);
    }

    @Override
    public DeletedImageDTO addLinks(boolean doInclude) {
        super.addLinks(doInclude);
        return this;
    }
}
