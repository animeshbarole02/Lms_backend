package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.dto.UsersOutDTO;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;

import java.util.Objects;

public class UsersMapper {


    private UsersMapper() {
    }

    public static UsersDTO mapToUsersDTO(Users users) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setId(users.getId());
        usersDTO.setName(users.getName());
        usersDTO.setEmail(users.getEmail());
        usersDTO.setPhoneNumber(users.getPhoneNumber());
        usersDTO.setRole(users.getRole());
        usersDTO.setPassword(users.getPassword());
        return usersDTO;
    }


    public static Users mapToUsers(UsersDTO usersDTO) {
        Users users = new Users();
        users.setId(usersDTO.getId());
        users.setName(usersDTO.getName());
        users.setEmail(usersDTO.getEmail());
        users.setPhoneNumber(usersDTO.getPhoneNumber());
        users.setRole(usersDTO.getRole());
        users.setPassword(usersDTO.getPassword());
        return users;
    }

    public  static UsersOutDTO maptoUsersOutDTO(Users users , UsersRepository usersRepository) {


        UsersOutDTO usersOutDTO = new UsersOutDTO();


            usersOutDTO.setId(users.getId());
            usersOutDTO.setEmail(users.getEmail());
            usersOutDTO.setName(users.getName());
            usersOutDTO.setPhoneNumber(users.getPhoneNumber());


            usersOutDTO.setRole(users.getRole());


        return  usersOutDTO;
    }
}
