package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.dto.BooksOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.repositories.CategoryRepository;



public class BooksMapper {

    private  BooksMapper(){

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


    public static BooksOutDTO mapToBooksOutDTO(Books books, CategoryRepository categoryRepository) {
        BooksOutDTO booksOutDTO = new BooksOutDTO();
        booksOutDTO.setId(books.getId());
        booksOutDTO.setTitle(books.getTitle());
        booksOutDTO.setAuthor(books.getAuthor());

        // Fetch the Category details using categoryId
        Category category = categoryRepository.findById(books.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + books.getCategoryId()));
        booksOutDTO.setCategory(category); // Set the fetched Category object

        booksOutDTO.setQuantity(books.getQuantity());
        return booksOutDTO;
    }


}
