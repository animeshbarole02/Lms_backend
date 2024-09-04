package com.nucleusTeq.backend.repositories;

import com.nucleusTeq.backend.entities.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

    Optional<Issuance> findByUserIdAndBookId(Long userId, Long bookId);
    Page<Issuance> findByUserId(Long userId, PageRequest pageRequest);
    Page<Issuance> findByBookId(Long bookId, PageRequest pageRequest);
    boolean existsByBookIdAndStatus(Long bookId, String status);
    void deleteByBookId(Long bookId);
    void deleteByBookIdAndStatus(Long bookId , String status);
}
