package com.elleined.image_server_api.dto.folder;

import com.elleined.image_server_api.dto.IntegerDTO;
import com.elleined.image_server_api.dto.project.ProjectDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;

import java.time.LocalDateTime;
import java.util.List;

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
    protected List<Link> getAllRelatedLinks(boolean doInclude) {
        return List.of();
    }

    @Override
    protected List<Link> getAllSelfLinks(boolean doInclude) {
        return List.of();
    }
}

