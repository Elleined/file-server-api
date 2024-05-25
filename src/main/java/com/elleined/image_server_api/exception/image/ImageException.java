package com.elleined.image_server_api.exception.image;

import com.elleined.image_server_api.exception.SystemException;

public class ImageException extends SystemException {
    public ImageException(String message) {
        super(message);
    }
}
