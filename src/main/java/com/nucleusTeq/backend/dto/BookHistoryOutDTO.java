package com.nucleusTeq.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
public class BookHistoryOutDTO {

    private Long id;

    @NotNull(message = "User cannot be null")
    private String name ;

    @NotNull(message = "Book cannot be null")
    private String email;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;

    private String status;

    private String issuanceType;
}
