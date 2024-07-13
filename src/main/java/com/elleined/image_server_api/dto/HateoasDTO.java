package com.elleined.image_server_api.dto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public abstract class HateoasDTO extends RepresentationModel<HateoasDTO> {
    public HateoasDTO addLinks(boolean doInclude) {
        this.addAllIf(doInclude, () -> getAllSelfLinks(doInclude));
        this.addAllIf(doInclude, () -> getAllRelatedLinks( doInclude));

        return this;
    }

    protected abstract List<Link> getAllRelatedLinks(boolean doInclude);
    protected abstract List<Link> getAllSelfLinks(boolean doInclude);
}