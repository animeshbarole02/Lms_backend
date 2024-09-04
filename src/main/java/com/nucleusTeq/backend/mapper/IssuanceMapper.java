package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.IssuanceDTO;
import com.nucleusTeq.backend.dto.IssuanceOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Issuance;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;

public class IssuanceMapper {

    private IssuanceMapper() {
    }

    public static IssuanceDTO mapToIssuanceDTO(Issuance issuance) {
        IssuanceDTO issuanceDTO = new IssuanceDTO();
        issuanceDTO.setId(issuance.getId());
        issuanceDTO.setUserId(issuance.getUserId());
        issuanceDTO.setBookId(issuance.getBookId());
        issuanceDTO.setIssuedAt(issuance.getIssuedAt());
        issuanceDTO.setReturnedAt(issuance.getReturnedAt());
        issuanceDTO.setExpectedReturn(issuance.getExpectedReturn());
        issuanceDTO.setStatus(issuance.getStatus());
        issuanceDTO.setIssuanceType(issuance.getIssuanceType());

        return issuanceDTO;
    }

    public static Issuance mapToIssuance(IssuanceDTO issuanceDTO) {
        Issuance issuance = new Issuance();
        issuance.setId(issuanceDTO.getId());
        issuance.setUserId(issuanceDTO.getUserId());
        issuance.setBookId(issuanceDTO.getBookId());
        issuance.setIssuedAt(issuanceDTO.getIssuedAt());
        issuance.setReturnedAt(issuanceDTO.getReturnedAt());
        issuance.setExpectedReturn(issuanceDTO.getExpectedReturn());
        issuance.setStatus(issuanceDTO.getStatus());
        issuance.setIssuanceType(issuanceDTO.getIssuanceType());

        return issuance;
    }

    public static IssuanceOutDTO mapToIssuanceOutDTO(Issuance issuance, BooksRepository bookRepository, UsersRepository userRepository) {
        IssuanceOutDTO issuanceOutDTO = new IssuanceOutDTO();

        issuanceOutDTO.setId(issuance.getId());
        issuanceOutDTO.setIssuedAt(issuance.getIssuedAt());
        issuanceOutDTO.setReturnedAt(issuance.getReturnedAt());
        issuanceOutDTO.setExpectedReturn(issuance.getExpectedReturn());
        issuanceOutDTO.setStatus(issuance.getStatus());
        issuanceOutDTO.setIssuanceType(issuance.getIssuanceType());

        // Fetch Book details using bookId
        Books book = bookRepository.findById(issuance.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + issuance.getBookId()));
        issuanceOutDTO.setBook(book);

        // Fetch User details using userId
        Users user = userRepository.findById(issuance.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + issuance.getUserId()));
        issuanceOutDTO.setUser(user);

        return issuanceOutDTO;
    }
}
