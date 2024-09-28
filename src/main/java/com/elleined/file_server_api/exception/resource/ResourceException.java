package com.elleined.file_server_api.exception.resource;

import com.elleined.file_server_api.exception.SystemException;

public class ResourceException extends SystemException {
    public ResourceException(String message) {
        super(message);
    }
}
