package com.elleined.file_server_api.validator;


import com.elleined.file_server_api.exception.field.EmailException;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    public void validate(String email) throws EmailException {
        if (!email.endsWith("@gmail.com")) throw new EmailException("Email must ends with @gmail.com!");
        if (email.startsWith("@")) throw new EmailException("Email should not starts with @!");
    }
}
