package com.nucleusTeq.backend.services;


import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.dto.BooksOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBooksService {

      String createBook(List<BooksDTO> booksDTOList);

      String deleteBook(Long id);

      String updateBook(Long id , BooksDTO booksDTO);

      Page<BooksOutDTO> getBooks(int page, int size, String search);

      BooksOutDTO getBookByTitle(String title);

      long getTotalBookCount();
}
