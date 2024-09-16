package com.nucleusTeq.backend.services.Impl;


import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.dto.BooksOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Category;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.exception.DataIntegrityViolationCustomException;
import com.nucleusTeq.backend.exception.MethodNotFoundException;
import com.nucleusTeq.backend.exception.ResourceConflictException;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.BooksMapper;
import com.nucleusTeq.backend.mapper.UsersMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.services.IBooksService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksServiceImp implements IBooksService {

    private final IssuanceRepository issuanceRepository;


    private final  BooksRepository booksRepository;


    private final CategoryRepository categoryRepository;

    @Override
    public  String createBook(BooksDTO booksDTO){

        Optional<Books> existingBook = booksRepository.findByTitle(booksDTO.getTitle());

        if (existingBook.isPresent()) {

            throw new ResourceConflictException("Book with title '" + booksDTO.getTitle() + "' already exists.");
        }


        Books bookToSave = BooksMapper.mapToBooks(booksDTO);


        booksRepository.save(bookToSave);

        return  "Book is Added";
    }

    @Transactional
    @Override
    public  String deleteBook(Long id) {

        Optional<Books> book = booksRepository.findById(id);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }

        boolean hasIssuedRecord = issuanceRepository.existsByBookIdAndStatus(id, "Issued");

        if (hasIssuedRecord) {
            throw new MethodNotFoundException("Book cannot be deleted as it is currently issued.");
        }


        issuanceRepository.deleteByBookId(id);

        booksRepository.deleteById(id);
        return "Book deleted successfully";

    }

    @Override
    public String updateBook(Long id , BooksDTO booksDTO){

        Optional<Books> optionalBooks = booksRepository.findById(id);

        if (optionalBooks.isPresent()) {

            Optional<Books> bookWithSameTitle = booksRepository.findByTitle(booksDTO.getTitle());

            if (bookWithSameTitle.isPresent() && !bookWithSameTitle.get().getId().equals(id)) {

                throw new ResourceConflictException("Book with the same title already exists.");
            }


            Books existingBook = optionalBooks.get();
            existingBook.setTitle(booksDTO.getTitle());
            existingBook.setAuthor(booksDTO.getAuthor());
            existingBook.setCategoryId(booksDTO.getCategoryId());
            existingBook.setQuantity(booksDTO.getQuantity());

            booksRepository.save(existingBook);

            return "Book updated successfully";
        } else {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }

    }




    @Override
    public Page<BooksOutDTO> getBooks(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Books> booksPage;

        if (search != null && !search.isEmpty()) {
            booksPage = booksRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search, pageable);
        } else {
            booksPage = booksRepository.findAll(pageable);
        }


        return booksPage.map(books -> BooksMapper.mapToBooksOutDTO(books, categoryRepository));
    }

    @Override
    public BooksOutDTO getBookByTitle(String title) {

        Optional<Books> booksOptional = booksRepository.findByTitle(title);

        if (booksOptional.isPresent()) {

            Books book = booksOptional.get();
            return BooksMapper.mapToBooksOutDTO(book, categoryRepository);
        } else {

            throw new UsernameNotFoundException("Book not found with title: " + title);
        }

    }


    @Override
    public long getTotalBookCount() {
        return booksRepository.count();
    }

    @Override
    public List<Books> findBooksByTitleContaining(String searchTerm) {
        return booksRepository.findByTitleContainingIgnoreCase(searchTerm);
    }

}
