package com.elleined.image_server_api.controller;

import com.elleined.image_server_api.dto.APIResponse;
import com.elleined.image_server_api.exception.field.FieldException;
import com.elleined.image_server_api.exception.image.ImageException;
import com.elleined.image_server_api.exception.resource.ResourceException;
import jakarta.transaction.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<APIResponse> handleResourceException(ResourceException ex) {
        var responseMessage = new APIResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            FieldException.class,
            ImageException.class,
            SystemException.class,
    })
    public ResponseEntity<APIResponse> handleSystemException(RuntimeException ex) {
        var responseMessage = new APIResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<APIResponse>> handleBindException(BindException ex) {
        List<APIResponse> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .map(errorMessage -> new APIResponse(HttpStatus.BAD_REQUEST, errorMessage))
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}