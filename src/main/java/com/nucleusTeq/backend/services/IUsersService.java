package com.nucleusTeq.backend.services;

import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.dto.UsersOutDTO;
import com.nucleusTeq.backend.entities.Users;
import org.springframework.data.domain.Page;

import java.util.List;
public interface IUsersService {

    String createUser(UsersDTO usersDTO) ;

    UsersDTO getUserById(Long id);

    List<UsersDTO> getAllUsers();

    String updateUser(Long id , UsersDTO usersDTO);

    String deleteUser(Long id);

    Users getByUserName(String name);

    Page<UsersOutDTO> getUsers(int page, int size, String search);

    UsersOutDTO getUserByMobile(String number);

    long getUserCount();

}
