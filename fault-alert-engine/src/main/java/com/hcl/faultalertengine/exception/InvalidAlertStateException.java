package com.hcl.faultalertengine.exception;

public class InvalidAlertStateException extends RuntimeException{
    public InvalidAlertStateException(String message){
        super(message);
    }
}
