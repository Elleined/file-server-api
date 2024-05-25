package com.elleined.image_server_api.exception.resource;

import com.elleined.image_server_api.exception.SystemException;

public class ResourceException extends SystemException {
    public ResourceException(String message) {
        super(message);
    }
}
