package com.elleined.image_server_api.dto.format;

import com.elleined.image_server_api.dto.IntegerDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FormatDTO extends IntegerDTO {
    private String format;

    @Builder
    public FormatDTO(LocalDateTime createdAt, int id, String format) {
        super(createdAt, id);
        this.format = format;
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
