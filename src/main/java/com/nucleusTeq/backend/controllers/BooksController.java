package com.nucleusTeq.backend.controllers;

import com.nucleusTeq.backend.dto.BooksDTO;
import com.nucleusTeq.backend.dto.BooksOutDTO;
import com.nucleusTeq.backend.dto.ResponseDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.services.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.nucleusTeq.backend.constants.Constants.OK_STATUS;

@RestController
@RequestMapping(value = "api/v1/books")
public class BooksController {


    @Autowired
    private IBooksService iBooksService;


    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createBook(@RequestBody BooksDTO booksDTO){

        String message = iBooksService.createBook(booksDTO);

        return  ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(OK_STATUS,message));

    }


    @CrossOrigin
    @PatchMapping("/update/{id}")
    public  ResponseEntity<ResponseDTO> updateBook(@PathVariable Long id , @RequestBody BooksDTO booksDTO) {

        String message  = iBooksService.updateBook(id,booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS,message));


    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<ResponseDTO> deleteBook(@PathVariable Long id) {
        String message = iBooksService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(OK_STATUS,message));
    }

    @CrossOrigin
    @GetMapping("/list")
    public Page<BooksOutDTO> getBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
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


    @CrossOrigin
    @GetMapping("/suggestions")
    public List<Books> getBookSuggestions(@RequestParam String searchTerm) {
        return iBooksService.findBooksByTitleContaining(searchTerm);
    }

}
