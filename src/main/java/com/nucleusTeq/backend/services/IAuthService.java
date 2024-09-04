package com.nucleusTeq.backend.services;
import com.nucleusTeq.backend.dto.LoginDTO;
import com.nucleusTeq.backend.jwt.LoginResponse;

public interface IAuthService {

    LoginResponse authenticateUser(LoginDTO loginDTO) throws Exception;


}
