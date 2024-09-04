package com.nucleusTeq.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class UsersOutDTO {

   Long id;
   String name;
   String email;
   String phoneNumber;
   String role;
}
