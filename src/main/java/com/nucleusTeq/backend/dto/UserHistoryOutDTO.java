package com.nucleusTeq.backend.dto;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;



@Data
public class UserHistoryOutDTO {

    private Long id;

    @NotNull(message = "User cannot be null")
    private String Book ;

    @NotNull(message = "Book cannot be null")
    private String Category;

    private LocalDateTime issuedAt;

    private LocalDateTime returnedAt;

    private String status;

    private String issuanceType;
}
