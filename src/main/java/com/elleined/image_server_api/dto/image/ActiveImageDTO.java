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
public class ActiveImageDTO extends ImageDTO {
    private byte[] bytes;

    @Builder
    public ActiveImageDTO(LocalDateTime createdAt, UUID id, LocalDateTime lastAccessedAt, String description, String additionalInformation, FormatDTO formatDTO, String fileName, FolderDTO folderDTO, double fileSizeInMB, byte[] bytes) {
        super(createdAt, id, lastAccessedAt, description, additionalInformation, formatDTO, fileName, folderDTO, fileSizeInMB);
        this.bytes = bytes;
    }

    @Override
    public ActiveImageDTO addLinks(boolean doInclude) {
        super.addLinks(doInclude);
        return this;
    }
}
