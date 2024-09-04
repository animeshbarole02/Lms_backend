package com.nucleusTeq.backend.controllers;


import com.nucleusTeq.backend.dto.UserHistoryOutDTO;
import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.dto.UsersOutDTO;

import com.nucleusTeq.backend.jwt.JwtUtils;
import com.nucleusTeq.backend.services.IUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.web.bind.annotation.*;

import java.util.List;


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


    @CrossOrigin
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UsersDTO usersDTO) {
        String response =iUsersService.updateUser(id, usersDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = iUsersService.deleteUser(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<Page<UsersOutDTO>> getUsersByList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search
    ){

        Page<UsersOutDTO> usersOutDTOS = iUsersService.getUsers(page, size, search);
        return  ResponseEntity.status(HttpStatus.OK).body(usersOutDTOS);
    }




    @CrossOrigin
    @GetMapping("/number/{number}")
    public  ResponseEntity<UsersOutDTO> getUserByMobile(@PathVariable String number) {

        UsersOutDTO usersOutDTO = iUsersService.getUserByMobile(number);

        return ResponseEntity.status(HttpStatus.OK).body(usersOutDTO);
    }


    @CrossOrigin
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = iUsersService.getUserCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }


}
