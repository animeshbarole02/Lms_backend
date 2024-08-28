package com.nucleusTeq.backend.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {

    private Long id;



    @NotEmpty(message = "name cannot be Empty")
    private String name;


    @NotEmpty(message = " email cannot be Empty")
    private String email;

    @NotEmpty(message = "Phone Number name cannot be Empty")
    private String phoneNumber;

    @NotEmpty(message = "Role name cannot be Empty")
    private String role;

    @NotEmpty(message = "Password name cannot be Empty")
    private String password;
}
