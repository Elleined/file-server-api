package com.elleined.image_server_api.exception.resource;

public class ResourceNotOwnedException extends ResourceException {

    public ResourceNotOwnedException(String message) {
        super(message);
    }
}
