package com.elleined.image_server_api.exception.field;

import com.elleined.image_server_api.exception.SystemException;

public class FieldException extends SystemException {
    public FieldException(String message) {
        super(message);
    }
}
