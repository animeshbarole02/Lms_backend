package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.BookHistoryOutDTO;
import com.nucleusTeq.backend.dto.UserHistoryOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.entities.Issuance;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;

public class HistoryMapper {

    public static UserHistoryOutDTO mapToUserHistoryOutDTO(Issuance issuance, BooksRepository booksRepository, CategoryRepository categoriesRepository) {
        UserHistoryOutDTO userHistoryOutDTO = new UserHistoryOutDTO();

        userHistoryOutDTO.setId(issuance.getId());
        userHistoryOutDTO.setIssuedAt(issuance.getIssuedAt());
        userHistoryOutDTO.setReturnedAt(issuance.getReturnedAt());
        userHistoryOutDTO.setStatus(issuance.getStatus());
        userHistoryOutDTO.setIssuanceType(issuance.getIssuanceType());


        Books book = booksRepository.findById(issuance.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + issuance.getBookId()));
        userHistoryOutDTO.setBook(book.getTitle());


        Category category = categoriesRepository.findById(book.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + book.getCategoryId()));
        userHistoryOutDTO.setCategory(category.getName());

        return userHistoryOutDTO;
    }

    public  static BookHistoryOutDTO mapToBookHistoryOutDTO(Issuance issuance , UsersRepository usersRepository) {

        BookHistoryOutDTO bookHistoryOutDTO = new BookHistoryOutDTO();

        bookHistoryOutDTO.setId(issuance.getId());
        bookHistoryOutDTO.setIssuedAt(issuance.getIssuedAt());
        bookHistoryOutDTO.setReturnedAt(issuance.getReturnedAt());
        bookHistoryOutDTO.setStatus(issuance.getStatus());
        bookHistoryOutDTO.setIssuanceType(issuance.getIssuanceType());

        Users user = usersRepository.findById(issuance.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found for this id :" + issuance.getUserId()));
        bookHistoryOutDTO.setName(user.getName());
        bookHistoryOutDTO.setEmail(user.getEmail());



        return  bookHistoryOutDTO;
    }
}
