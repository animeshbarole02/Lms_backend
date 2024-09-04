package com.nucleusTeq.backend.dto;

import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Users;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class IssuanceOutDTO {

    private Long id;

    @NotNull(message = "User cannot be null")
    private Users user;

    @NotNull(message = "Book cannot be null")
    private Books book;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;

    @NotNull(message = "Expected Return cannot be null")
    private LocalDateTime expectedReturn;

    private String status;

    private String issuanceType;
}
