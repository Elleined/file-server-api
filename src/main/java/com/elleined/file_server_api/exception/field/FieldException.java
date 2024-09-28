package com.elleined.file_server_api.exception.field;

import com.elleined.file_server_api.exception.SystemException;

public class FieldException extends SystemException {
    public FieldException(String message) {
        super(message);
    }
}
