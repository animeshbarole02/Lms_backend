package com.nucleusTeq.backend.exception;

public class MethodNotFoundException  extends  RuntimeException{
    public MethodNotFoundException(String message) {
        super(message);
    }
}
