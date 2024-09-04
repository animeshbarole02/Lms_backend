package com.nucleusTeq.backend.repositories;

import com.nucleusTeq.backend.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhoneNumber(String phoneNumber);

   Page<Users> findByNameContainingIgnoreCaseAndRoleEquals(String search, String role,Pageable pageable);
    Page<Users> findByRoleEquals(String role, Pageable pageable);
}
