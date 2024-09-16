package com.nucleusTeq.backend.exception;

import com.nucleusTeq.backend.dto.ResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage()));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ResponseDTO> handleResourceConflict(ResourceConflictException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO(String.valueOf(HttpStatus.CONFLICT.value()), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationCustomException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationCustomException(DataIntegrityViolationCustomException ex) {
        ResponseDTO errorResponseDto = new ResponseDTO(
                String.valueOf(HttpStatus.CONFLICT.value()),
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleMethodNotFoundException(MethodNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ResponseDTO(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), ex.getMessage()));
    }

}
