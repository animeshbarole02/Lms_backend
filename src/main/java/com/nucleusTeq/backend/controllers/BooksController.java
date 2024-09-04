package com.nucleusTeq.backend.controllers;

import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.dto.BooksOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.services.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/books")
public class BooksController {


    @Autowired
    private IBooksService iBooksService;


    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createBook(@RequestBody List<BooksDTO> booksDTOList){

        String message = iBooksService.createBook(booksDTOList);

        return  ResponseEntity.status(HttpStatus.CREATED).body(message);

    }


    @CrossOrigin
    @PatchMapping("/update/{id}")
    public  ResponseEntity<String> updateBook(@PathVariable Long id , @RequestBody BooksDTO booksDTO) {

         String message  = iBooksService.updateBook(id,booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(message);


    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<String> deleteBook(@PathVariable Long id) {
        String message = iBooksService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @CrossOrigin
    @GetMapping("/list")
    public Page<BooksOutDTO> getBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search
    ) {
        return iBooksService.getBooks(page,size,search);
    }


    @CrossOrigin
    @GetMapping("/getByTitle/{title}")
        public ResponseEntity<BooksOutDTO> getBookByTitle(@PathVariable String title) {

          BooksOutDTO booksOutDTO = iBooksService.getBookByTitle(title);
         return ResponseEntity.status(HttpStatus.OK).body(booksOutDTO);

        }


    @CrossOrigin
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalBookCount() {
        long count = iBooksService.getTotalBookCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

}
