package com.nucleusTeq.backend.dto;


import lombok.Data;



@Data
public class UsersOutDTO {

   Long id;
   String name;
   String email;
   String phoneNumber;
   String role;
}
