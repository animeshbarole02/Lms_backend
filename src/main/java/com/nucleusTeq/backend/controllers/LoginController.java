package com.nucleusTeq.backend.controllers;

import com.nucleusTeq.backend.dto.LoginDTO;

import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.dto.UsersOutDTO;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.jwt.JwtUtils;
import com.nucleusTeq.backend.jwt.LoginResponse;

import com.nucleusTeq.backend.mapper.UsersMapper;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.IUsersService;
import com.nucleusTeq.backend.services.Impl.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "api/v1")
public class LoginController {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IUsersService iUsersService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthService authService; // Use AuthService instead of directly using service implementations

    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        try {
            // Delegate authentication logic to the AuthService
            LoginResponse response = authService.authenticateUser(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("Message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin
    @GetMapping("/currentUser")
    public ResponseEntity<UsersDTO> currentUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);
        Users user = iUsersService.getByUserName(userName);
        UsersDTO usersDTO = UsersMapper.mapToUsersDTO(user);

        return ResponseEntity.status(HttpStatus.OK).body(usersDTO);

    }

}
