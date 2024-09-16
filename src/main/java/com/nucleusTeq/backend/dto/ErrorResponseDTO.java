package com.nucleusTeq.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDTO {

        private String message;
        private String errorCode;



        // Getters and setters
}
