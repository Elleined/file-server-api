package com.elleined.image_server_api.service;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.model.PrimaryKeyIdentity;

import java.util.List;

public interface CustomService<ENTITY extends PrimaryKeyIdentity> {
    List<ENTITY> getAllById(List<Integer> ids);
}