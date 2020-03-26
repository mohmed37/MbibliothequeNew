package com.microservicelibrairie.web.exceptions;

public class ImpossibleAjouterUneReservationException  extends RuntimeException  {
    public ImpossibleAjouterUneReservationException(String message) {
        super(message);
    }
}
