package com.nucleusTeq.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class LoginDTO {

    private  String usernameOrPhoneNumber;
    private  String password;

}
