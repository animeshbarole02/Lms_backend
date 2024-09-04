package com.nucleusTeq.backend.services.Impl;


import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.dto.BooksOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.BooksMapper;
import com.nucleusTeq.backend.mapper.UsersMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.services.IBooksService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BooksServiceImp implements IBooksService {


    @Autowired
    private IssuanceRepository issuanceRepository;

    @Autowired
    private  BooksRepository booksRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

        Optional<Books> book = booksRepository.findById(id);
        if (book.isEmpty()) {
            return  "Book not found";
        }

        boolean hasIssuedRecord = issuanceRepository.existsByBookIdAndStatus(id, "Issued");

        if (hasIssuedRecord) {
            return  "Book cannot be deleted as it is currently issued.";
        }

        issuanceRepository.deleteByBookIdAndStatus(id, "Returned");

        booksRepository.deleteById(id);
        return "Book deleted successfully";

    }

    @Override
    public String updateBook(Long id , BooksDTO booksDTO){

        Optional<Books> optionalBooks = booksRepository.findById(id);

        System.out.println(id);
        System.out.println(booksDTO.getCategoryId());

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
    public Page<BooksOutDTO> getBooks(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Books> booksPage;

        if (search != null && !search.isEmpty()) {
            booksPage = booksRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else {
            booksPage = booksRepository.findAll(pageable);
        }

        // Map the Page<Books> to Page<BooksOutDTO> using the mapper and pass categoryRepository
        return booksPage.map(books -> BooksMapper.mapToBooksOutDTO(books, categoryRepository));
    }

    @Override
    public BooksOutDTO getBookByTitle(String title) {

        Optional<Books> booksOptional = booksRepository.findByTitle(title);

        if (booksOptional.isPresent()) {
            // If user is present, convert to UsersOutDTO and return
            Books book = booksOptional.get();
            return BooksMapper.mapToBooksOutDTO(book, categoryRepository);
        } else {
            // If user is not present, throw an exception
            throw new UsernameNotFoundException("Book not found with title: " + title);
        }

    }


    @Override
    public long getTotalBookCount() {
        return booksRepository.count();
    }

}
