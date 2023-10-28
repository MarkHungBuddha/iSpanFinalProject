package com.peko.houshoukaizokudan.handler;

import java.io.Serializable;

public class InvalidPriceRangeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public InvalidPriceRangeException(String message) {
        super(message);
    }
}
