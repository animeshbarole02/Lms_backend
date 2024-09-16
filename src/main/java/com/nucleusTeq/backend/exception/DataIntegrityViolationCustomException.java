package com.nucleusTeq.backend.exception;

public class DataIntegrityViolationCustomException extends  RuntimeException{
    public DataIntegrityViolationCustomException(String message) {
        super(message);
    }
}
