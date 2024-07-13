package com.elleined.image_server_api.dto.folder;

import com.elleined.image_server_api.controller.folder.FolderController;
import com.elleined.image_server_api.controller.image.ActiveImageController;
import com.elleined.image_server_api.dto.IntegerDTO;
import com.elleined.image_server_api.dto.project.ProjectDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class FolderDTO extends IntegerDTO {
    private String name;
    private ProjectDTO projectDTO;

    @Builder
    public FolderDTO(LocalDateTime createdAt, int id, String name, ProjectDTO projectDTO) {
        super(createdAt, id);
        this.name = name;
        this.projectDTO = projectDTO;
    }

    @Override
    public FolderDTO addLinks(boolean doInclude) {
        super.addLinks(doInclude);
        return this;
    }

    @Override
    protected List<Link> getAllRelatedLinks(boolean doInclude) throws IOException {
        return List.of(
                linkTo(methodOn(ActiveImageController.class)
                        .save(this.getProjectDTO().getId(), 0, null, null, null, doInclude))
                        .withRel("active-image")
                        .withTitle("Save")
                        .withType(HttpMethod.POST.name()),

                linkTo(methodOn(ActiveImageController.class)
                        .getAll(this.getProjectDTO().getId(), 0, 0, 0, null, null, doInclude))
                        .withRel("active-image")
                        .withTitle("Get all")
                        .withType(HttpMethod.GET.name()),

                linkTo(methodOn(ActiveImageController.class)
                        .getByUUID(this.getProjectDTO().getId(), 0, null, doInclude))
                        .withRel("active-image")
                        .withTitle("Get by UUID")
                        .withType(HttpMethod.GET.name())
        );
    }

    @Override
    protected List<Link> getAllSelfLinks(boolean doInclude) throws IOException {
        return List.of(
                linkTo(methodOn(FolderController.class)
                        .save(this.getId(), null, doInclude))
                        .withSelfRel()
                        .withTitle("Save")
                        .withType(HttpMethod.POST.name()),

                linkTo(methodOn(FolderController.class)
                        .getAll(this.getId(), 0, 0, null, null, doInclude))
                        .withSelfRel()
                        .withTitle("Get all")
                        .withType(HttpMethod.GET.name())
        );
    }
}

