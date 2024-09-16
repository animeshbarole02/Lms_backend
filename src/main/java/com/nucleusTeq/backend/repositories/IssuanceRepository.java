package com.nucleusTeq.backend.repositories;

import com.nucleusTeq.backend.entities.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface IssuanceRepository extends JpaRepository<Issuance, Long> {


    Page<Issuance> findByUserId(Long userId, PageRequest pageRequest);
    Page<Issuance> findByBookId(Long bookId, PageRequest pageRequest);
    boolean existsByBookIdAndStatus(Long bookId, String status);
    void deleteByBookId(Long bookId);
    boolean existsByUserIdAndStatus(Long userId, String status);
    void deleteByUserId(Long id);



    @Query("SELECT i FROM Issuance i " +
            "JOIN Users u ON i.userId = u.id " +
            "JOIN Books b ON i.bookId = b.id " +
            "WHERE (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Issuance> findByBookTitleOrUserName(String search, Pageable pageable);


    @Query("SELECT i FROM Issuance i WHERE i.expectedReturn BETWEEN :start AND :end AND i.status = :status")
    List<Issuance> findAllByExpectedReturnTimeBetweenAndStatus(
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") String status);
}


