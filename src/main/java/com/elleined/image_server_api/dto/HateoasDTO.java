package com.elleined.image_server_api.dto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.io.IOException;
import java.util.List;

public abstract class HateoasDTO extends RepresentationModel<HateoasDTO> {

    public HateoasDTO addLinks(boolean doInclude) {
        this.addAllIf(doInclude, () -> {
            try {
                return getAllSelfLinks(doInclude);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.addAllIf(doInclude, () -> {
            try {
                return getAllRelatedLinks(doInclude);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return this;
    }

    protected abstract List<Link> getAllRelatedLinks(boolean doInclude) throws IOException;
    protected abstract List<Link> getAllSelfLinks(boolean doInclude) throws IOException;
}