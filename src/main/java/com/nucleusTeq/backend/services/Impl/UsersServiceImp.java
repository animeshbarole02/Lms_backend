package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.dto.UsersDTO;
import com.nucleusTeq.backend.dto.UsersOutDTO;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.exception.MethodNotFoundException;
import com.nucleusTeq.backend.exception.ResourceConflictException;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.UsersMapper;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.ISMSService;
import com.nucleusTeq.backend.services.IUsersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequiredArgsConstructor
public class UsersServiceImp implements IUsersService , UserDetailsService {


    private final ISMSService ismsService;

    private final UsersRepository usersRepository;


    private final IssuanceRepository issuanceRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public String createUser(UsersDTO usersDTO) {

        Optional<Users> userWithSameNumber = usersRepository.findByPhoneNumber(usersDTO.getPhoneNumber());
        Optional<Users> userWithSameEmail = usersRepository.findByEmail(usersDTO.getEmail());

        if (userWithSameNumber.isPresent() || userWithSameEmail.isPresent()) {
            throw new ResourceConflictException("User with this email or phone number already exists.");
        }

        Users user = UsersMapper.mapToUsers(usersDTO);

        String Password = usersDTO.getPassword();
         user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser =  usersRepository.save(user);

        String message = String.format( "\nWelcome %s\n" +
                        "Your Account is Open on Readify Library Manager\n" +
                        "These are your login credentials\n" +
                        "Username: %s (OR) %s\n" +
                        "Password: %s",
                savedUser.getName(),
                savedUser.getPhoneNumber(),
                savedUser.getEmail(),
                Password);
        ismsService.verifyNumber(savedUser.getPhoneNumber());
        ismsService.sendSms(savedUser.getPhoneNumber(), message);

        return "User added successfully ";

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));


        if (usersDTO.getEmail() != null) {
            Optional<Users> userWithSameEmail = usersRepository.findByEmail(usersDTO.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                throw new ResourceConflictException("Email already exists for another user.");
            }
            existingUser.setEmail(usersDTO.getEmail());
        }
        if (usersDTO.getPhoneNumber() != null) {
            Optional<Users> userWithSamePhoneNumber = usersRepository.findByPhoneNumber(usersDTO.getPhoneNumber());
            if (userWithSamePhoneNumber.isPresent() && !userWithSamePhoneNumber.get().getId().equals(id)) {
                throw new ResourceConflictException("Phone number already exists for another user.");
            }
            existingUser.setPhoneNumber(usersDTO.getPhoneNumber());
        }


        if (usersDTO.getName() != null) {
            existingUser.setName(usersDTO.getName());
        }


        Users updatedUser = usersRepository.save(existingUser);
        UsersMapper.mapToUsersDTO(updatedUser);

        return "User updated successfully";
    }

    @Transactional
    @Override

        public String deleteUser(Long id) {
            Optional<Users> user = usersRepository.findById(id);

            if (user.isEmpty()) {
                return "User not Found";
            }

            boolean hasIssuedRecord = issuanceRepository.existsByUserIdAndStatus(id, "Issued");

            if (hasIssuedRecord) {
                throw new MethodNotFoundException("User cannot be deleted as it is currently issued.");
            }

            issuanceRepository.deleteByUserId(id);
            usersRepository.deleteById(id);

            return "User deleted successfully";
        }

    @Override
   public Page<UsersOutDTO> getUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createdAt"));
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

            user = usersRepository.findByEmail(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name)
            );
        } else {

            user = usersRepository.findByPhoneNumber(name).orElseThrow(
                    () -> new UsernameNotFoundException("User not found for " + name)
            );
        }

        return user;
    }


    @Override
  public UsersOutDTO getUserByMobile(String number) {


        Optional<Users> userOptional = usersRepository.findByPhoneNumber(number);

        if (userOptional.isPresent()) {

            Users user = userOptional.get();
            return UsersMapper.maptoUsersOutDTO(user, usersRepository);
        } else {

            throw new MethodNotFoundException("User not found with phone number");
        }


    }

    @Override
    public long getUserCount() {
        return usersRepository.count();
    }



}
