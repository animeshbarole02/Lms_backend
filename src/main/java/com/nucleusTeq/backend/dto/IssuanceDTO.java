package com.nucleusTeq.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssuanceDTO {

    private Long id;

    @NotNull(message = "User cannot be null")
    private Long userId;

    @NotNull(message = "Book cannot be null")
    private Long bookId;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;


    private LocalDateTime expectedReturn;


    private String status;

    @NotEmpty(message = "Issuance Type cannot be empty")
    private String issuanceType;
}
