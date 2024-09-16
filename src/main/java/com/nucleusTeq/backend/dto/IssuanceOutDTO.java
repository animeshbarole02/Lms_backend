package com.nucleusTeq.backend.dto;

import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Users;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class IssuanceOutDTO {

    private Long id;

    private Long user_id;
    private  Long book_id;
    private Users user;
    private Books book;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;

    @NotNull(message = "Expected Return cannot be null")
    private LocalDateTime expectedReturn;

    private String status;

    private String issuanceType;
}
