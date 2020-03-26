package com.microservicelibrairie.web.exceptions;

public class LivreNotFoundException extends RuntimeException {
    public LivreNotFoundException(String message) {
        super(message);
    }
}
