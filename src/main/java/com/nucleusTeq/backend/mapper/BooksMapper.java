package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.repositories.CategoryRepository;

public class BooksMapper {

    private  BooksMapper(){

    }

    public static BooksDTO maptoBooksDTO(Books books)
    {
          BooksDTO booksDTO = new BooksDTO();
          booksDTO.setId(books.getId());
          booksDTO.setTitle(books.getTitle());
          booksDTO.setAuthor(books.getAuthor());
          booksDTO.setCategoryId(books.getCategoryId());
          booksDTO.setQuantity(books.getQuantity());


          return booksDTO;


    }

    public  static  Books mapToBooks(BooksDTO booksDTO)
    {
        Books book =  new Books();
        book.setId(booksDTO.getId());
        book.setTitle(booksDTO.getTitle());
        book.setAuthor(booksDTO.getAuthor());
        book.setCategoryId(booksDTO.getCategoryId());
        book.setQuantity(booksDTO.getQuantity());


        return book;

    }
}
