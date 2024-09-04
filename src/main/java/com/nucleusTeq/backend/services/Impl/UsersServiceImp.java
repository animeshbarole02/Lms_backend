package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.dto.UsersOutDTO;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.mapper.UsersMapper;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.IUsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersServiceImp implements IUsersService , UserDetailsService {


    @Autowired
    private UsersRepository usersRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public String createUser(UsersDTO usersDTO) {

        Users user = UsersMapper.mapToUsers(usersDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser =  usersRepository.save(user);

        return "User added successfully with ID: " + savedUser.getId();

    }

    @Override
   public UsersDTO getUserById(Long id){

          Users user =  usersRepository.findById(id)
                  .orElseThrow(()-> new RuntimeException("User not found with ID" + id));


          return  UsersMapper.mapToUsersDTO(user);

    }

    @Override
    public List<UsersDTO> getAllUsers(){
              List<Users> usersList = usersRepository.findAll();
              List<UsersDTO> usersDTOList = new ArrayList<>();

              usersList.forEach(user-> {
                  usersDTOList.add(UsersMapper.mapToUsersDTO(user));
              });


              return  usersDTOList;
    }

    @Override
    public String updateUser(Long id, UsersDTO usersDTO) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Only update fields that are not null in the incoming DTO
        if (usersDTO.getName() != null) {
            existingUser.setName(usersDTO.getName());
        }
        if (usersDTO.getEmail() != null) {
            existingUser.setEmail(usersDTO.getEmail());
        }
        if (usersDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(usersDTO.getPhoneNumber());
        }
        // Optionally check for other fields if needed
        // if (usersDTO.getRole() != null) {
        //     existingUser.setRole(usersDTO.getRole());
        // }
        // if (usersDTO.getPassword() != null) {
        //     existingUser.setPassword(usersDTO.getPassword());
        // }

        Users updatedUser = usersRepository.save(existingUser);
        UsersMapper.mapToUsersDTO(updatedUser);

        return "User updated successfully with ID: " + updatedUser.getId();
    }

    @Override
    public  String deleteUser(Long id){
          usersRepository.deleteById(id);

          return  "User deleted successfully with ID:" + id;
    }


    @Override
   public Page<UsersOutDTO> getUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Users> usersPage;
        if(search!=null && !search.isEmpty()) {
            usersPage =  usersRepository.findByNameContainingIgnoreCaseAndRoleEquals(search,"USER",pageable);
        }
        else {
            usersPage =   usersRepository.findByRoleEquals("USER", pageable);
        }

        return  usersPage.map(users -> UsersMapper.maptoUsersOutDTO(users,usersRepository));
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNumber) throws UsernameNotFoundException {

        Optional<Users> userOptional;

        if(usernameOrPhoneNumber.contains("@")) {
            userOptional = usersRepository.findByEmail(usernameOrPhoneNumber);
        }
        else {
            userOptional = usersRepository.findByPhoneNumber(usernameOrPhoneNumber);
        }
        Users userInfo = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + usernameOrPhoneNumber));



        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+userInfo.getRole()));

        return new User(userInfo.getEmail(), userInfo.getPassword(), grantedAuthorities);
    }

    @Override
    public Users getByUserName(String name) {
        Users user;
        if (name.contains("@")) {
//            email
            user = usersRepository.findByEmail(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name)
            );
        } else {
//            mobile
            user = usersRepository.findByPhoneNumber(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name)
            );
        }

        return user;
    }


    @Override
  public UsersOutDTO getUserByMobile(String number) {


        Optional<Users> userOptional = usersRepository.findByPhoneNumber(number);

        // Check if the user is present
        if (userOptional.isPresent()) {
            // If user is present, convert to UsersOutDTO and return
            Users user = userOptional.get();
            return UsersMapper.maptoUsersOutDTO(user, usersRepository);
        } else {
            // If user is not present, throw an exception
            throw new UsernameNotFoundException("User not found with phone number: " + number);
        }


    }

    @Override
    public long getUserCount() {
        return usersRepository.count();
    }



}
