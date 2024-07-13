package com.elleined.image_server_api.dto.project;

import com.elleined.image_server_api.dto.IntegerDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;

import java.time.LocalDateTime;
import java.util.List;

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
    protected List<Link> getAllRelatedLinks(boolean doInclude) {
        return List.of();
    }

    @Override
    protected List<Link> getAllSelfLinks(boolean doInclude) {
        return List.of();
    }
}
