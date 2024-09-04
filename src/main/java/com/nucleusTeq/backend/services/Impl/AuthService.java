package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.services.IAuthService;
import com.nucleusTeq.backend.dto.LoginDTO;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.jwt.JwtUtils;
import com.nucleusTeq.backend.jwt.LoginResponse;

import com.nucleusTeq.backend.services.IUsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IUsersService usersService; // Use interface for service dependency

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse authenticateUser(LoginDTO loginDTO) throws Exception {
        Authentication authentication = authenticate(loginDTO.getUsernameOrPhoneNumber(), loginDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Users user = usersService.getByUserName(username);
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        return new LoginResponse(jwtToken, userDetails.getUsername(), user.getName(), "ROLE_" + user.getRole(), user.getId());
    }

    private Authentication authenticate(String usernameOrPhoneNumber, String password) throws Exception {
        if (isEmail(usernameOrPhoneNumber)) {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrPhoneNumber, password));
        } else if (isPhoneNumber(usernameOrPhoneNumber)) {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrPhoneNumber, password));
        } else {
            throw new Exception("Invalid login input format.");
        }
    }

    private boolean isEmail(String input) {
        return input != null && input.contains("@");
    }

    private boolean isPhoneNumber(String input) {
        return input != null && input.matches("\\d+");
    }
}
