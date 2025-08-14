package com.stefandragomiroiu.rideshare_api.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public <T> ResourceNotFoundException(Class<T> cls, Long id) {
        super(cls.getSimpleName() + " with id " + id + " does not exist!");
    }

}
