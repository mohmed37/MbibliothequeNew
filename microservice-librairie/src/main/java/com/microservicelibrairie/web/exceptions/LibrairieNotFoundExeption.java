package com.microservicelibrairie.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LibrairieNotFoundExeption extends RuntimeException {
    public LibrairieNotFoundExeption(String message) {
        super(message);
    }
}
