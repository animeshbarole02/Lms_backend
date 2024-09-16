package com.nucleusTeq.backend.dto;
import lombok.Data;
import javax.validation.constraints.NotEmpty;


@Data

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
