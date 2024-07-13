package com.elleined.image_server_api.dto.project;

import com.elleined.image_server_api.controller.folder.FolderController;
import com.elleined.image_server_api.controller.project.ProjectController;
import com.elleined.image_server_api.dto.IntegerDTO;
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
public class ProjectDTO extends IntegerDTO {
    private String name;

    @Builder
    public ProjectDTO(LocalDateTime createdAt, int id, String name) {
        super(createdAt, id);
        this.name = name;
    }

    @Override
    public ProjectDTO addLinks(boolean doInclude) {
        super.addLinks(doInclude);
        return this;
    }

    @Override
    protected List<Link> getAllRelatedLinks(boolean doInclude) throws IOException {
        return List.of(
                linkTo(methodOn(FolderController.class)
                        .save(this.getId(), null, doInclude))
                        .withRel("folder")
                        .withTitle("Save")
                        .withType(HttpMethod.POST.name()),

                linkTo(methodOn(FolderController.class)
                        .getAll(this.getId(), 0, 0, null, null, doInclude))
                        .withRel("folder")
                        .withTitle("Get all")
                        .withType(HttpMethod.GET.name())
        );
    }

    @Override
    protected List<Link> getAllSelfLinks(boolean doInclude) throws IOException {
        return List.of(
                linkTo(methodOn(ProjectController.class)
                        .save(null, null, doInclude))
                        .withSelfRel()
                        .withTitle("Get all")
                        .withType(HttpMethod.GET.name())
        );
    }
}
