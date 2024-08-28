package com.nucleusTeq.backend.services.Impl;


import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.BooksMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.services.IBooksService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BooksServiceImp implements IBooksService {

    @Autowired
    private final BooksRepository booksRepository;


    @Override
    public  String createBook(List<BooksDTO> booksDTOList){

        List<Books> books = new ArrayList<>();

        booksDTOList.forEach(booksDTO -> {
            books.add(BooksMapper.mapToBooks(booksDTO));
        });

        List<Books> savedBooks = booksRepository.saveAll(books);


       return savedBooks.size() + "Book Added SuccessFully" ;
    }


    @Override
    public  String deleteBook(Long id) {
        booksRepository.deleteById(id);
        return "Book deleted successfully with ID: " + id;

    }

    @Override
    public  String updateBook(Long id , BooksDTO booksDTO){

        Optional<Books> optionalBooks = booksRepository.findById(id);

        if(optionalBooks.isPresent()) {
            Books existingBook = optionalBooks.get();
            existingBook.setTitle(booksDTO.getTitle());
            existingBook.setAuthor(booksDTO.getAuthor());
            existingBook.setCategoryId(booksDTO.getCategoryId());
            existingBook.setQuantity(booksDTO.getQuantity());
            booksRepository.save(existingBook);

            return  "Book updated successfully with ID: " + id;
        }else {
            throw  new ResourceNotFoundException("Book not fount with ID : "+ id);
        }

    }


    @Override
    public Page<Books> getBooks(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        if (search != null && !search.isEmpty()) {

            return booksRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else {

            return booksRepository.findAll(pageable);
        }
    }

}
