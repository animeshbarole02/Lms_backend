package com.nucleusTeq.backend.controllers;

import com.nucleusTeq.backend.dto.LoginDTO;
import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.jwt.JwtUtils;
import com.nucleusTeq.backend.services.IUsersService;
import com.nucleusTeq.backend.services.Impl.UsersServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "api/v1/users")


public class UsersController {


    @Autowired
    private IUsersService iUsersService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;



    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UsersDTO usersDTO){

        System.out.println("Create");
         String response = iUsersService.createUser(usersDTO);

        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UsersDTO>> getAllUsers() {



        System.out.println("I");
        List<UsersDTO> usersList = iUsersService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/{id}")

    public  ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        UsersDTO user  =  iUsersService.getUserById(id);

        return  ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @RequestBody UsersDTO usersDTO) {
        UsersDTO updatedUser =iUsersService.updateUser(id, usersDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = iUsersService.deleteUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

//
//    private boolean isEmail(String input) {
//        // Basic email validation logic
//        return input != null && input.contains("@");
//    }
//
//    private boolean isPhoneNumber(String input) {
//        // Basic phone number validation logic
//        return input != null && input.matches("\\d+"); // Simple check for numeric values
//    }




}
