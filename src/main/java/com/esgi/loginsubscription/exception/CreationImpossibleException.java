package com.esgi.loginsubscription.exception;

public class CreationImpossibleException extends RuntimeException{

    public CreationImpossibleException(String message) {
        super(message);
    }

    public CreationImpossibleException(String message, Throwable cause) {
        super(message, cause);
    }

}
