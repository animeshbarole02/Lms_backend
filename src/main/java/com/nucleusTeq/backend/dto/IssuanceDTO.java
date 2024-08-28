package com.nucleusTeq.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceDTO {


     private  Long id;

    @NotNull(message = "Book cannot be null")
     private  Long userId;

     @NotNull(message = "Book cannot be null")
     private  Long bookId;

     private Timestamp issueAt;

     private  Timestamp returnedAt;
    @NotEmpty(message = "Status  cannot be Empty")
     private  String status ;

    @NotEmpty(message = "Issuance Type cannot be Empty")
     private  String issuanceType;

}
