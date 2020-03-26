package com.microserviceuser.web.exeception;

public class ImpossibleAjouterUnUserException extends RuntimeException {
    public ImpossibleAjouterUnUserException(String message) {
        super(message);
    }
}
