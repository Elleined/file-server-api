package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.controller.image.ActiveImageController;
import com.elleined.image_server_api.controller.image.DeletedImageController;
import com.elleined.image_server_api.dto.folder.FolderDTO;
import com.elleined.image_server_api.dto.format.FormatDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @Override
    protected List<Link> getAllRelatedLinks(boolean doInclude) throws IOException {
        return List.of(
                linkTo(methodOn(ActiveImageController.class)
                        .save(this.getFolderDTO().getProjectDTO().getId(), this.getFolderDTO().getId(), null, null, null, doInclude))
                        .withRel("active-image")
                        .withTitle("Save")
                        .withType(HttpMethod.POST.name()),

                linkTo(methodOn(ActiveImageController.class)
                        .getAll(this.getFolderDTO().getProjectDTO().getId(), this.getFolderDTO().getId(), 0, 0, null, null, doInclude))
                        .withRel("active-image")
                        .withTitle("Get all")
                        .withType(HttpMethod.GET.name()),

                linkTo(methodOn(ActiveImageController.class)
                        .getByUUID(this.getFolderDTO().getProjectDTO().getId(), this.getFolderDTO().getId(), this.getId(), doInclude))
                        .withRel("active-image")
                        .withTitle("Get by UUID")
                        .withType(HttpMethod.GET.name())
        );
    }

    @Override
    protected List<Link> getAllSelfLinks(boolean doInclude) throws IOException {
        return List.of(
                linkTo(methodOn(DeletedImageController.class)
                        .restore(this.getFolderDTO().getProjectDTO().getId(), this.getFolderDTO().getId(), this.getId(), doInclude))
                        .withSelfRel()
                        .withTitle("Restore image")
                        .withType(HttpMethod.PUT.name()),

                linkTo(methodOn(DeletedImageController.class)
                        .getAll(this.getFolderDTO().getProjectDTO().getId(), this.getFolderDTO().getId(), 0, 0, null, null, doInclude))
                        .withSelfRel()
                        .withTitle("Get all")
                        .withType(HttpMethod.GET.name()),

                linkTo(methodOn(DeletedImageController.class)
                        .getByUUID(this.getFolderDTO().getProjectDTO().getId(), this.getFolderDTO().getId(), this.getId(), doInclude))
                        .withSelfRel()
                        .withTitle("Get by UUID")
                        .withType(HttpMethod.GET.name())
        );
    }
}
