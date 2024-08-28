package com.nucleusTeq.backend.services;

import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.entities.Users;

import java.util.List;
public interface IUsersService {

    String createUser(UsersDTO usersDTO) ;

    UsersDTO getUserById(Long id);

    List<UsersDTO> getAllUsers();

    UsersDTO updateUser(Long id , UsersDTO usersDTO);

    String deleteUser(Long id);

    Users getByUserName(String name);

}
