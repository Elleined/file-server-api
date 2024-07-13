package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.dto.UUIDDTO;
import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.dto.format.FormatDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ImageDTO extends UUIDDTO {
    private LocalDateTime lastAccessedAt;
    private String description;
    private String additionalInformation;
    private FormatDTO formatDTO;
    private String fileName;
    private FolderDTO folderDTO;
    private double fileSizeInMB;

    @Builder
    public ImageDTO(LocalDateTime createdAt, UUID id, LocalDateTime lastAccessedAt, String description, String additionalInformation, FormatDTO formatDTO, String fileName, FolderDTO folderDTO, double fileSizeInMB) {
        super(createdAt, id);
        this.lastAccessedAt = lastAccessedAt;
        this.description = description;
        this.additionalInformation = additionalInformation;
        this.formatDTO = formatDTO;
        this.fileName = fileName;
        this.folderDTO = folderDTO;
        this.fileSizeInMB = fileSizeInMB;
    }

    @Override
    public ImageDTO addLinks(boolean doInclude) {
        super.addLinks(doInclude);
        return this;
    }

    @Override
    protected List<Link> getAllRelatedLinks(boolean doInclude) {
        return List.of();
    }

    @Override
    protected List<Link> getAllSelfLinks(boolean doInclude) {
        return List.of();
    }
}
