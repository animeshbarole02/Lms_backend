package com.nucleusTeq.backend.controllers;


import com.nucleusTeq.backend.dto.*;
import com.nucleusTeq.backend.services.IIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/issuances")
public class IssuanceController {

    @Autowired
    private  IIssuanceService iIssuanceService;


    @GetMapping("/getAll")
    public ResponseEntity<List<IssuanceDTO>> getAllIssuances() {

        List<IssuanceDTO> issuanceDTOList = iIssuanceService.getAllIssuances();
        return  ResponseEntity.status(HttpStatus.OK).body(issuanceDTOList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<IssuanceDTO> getIssuanceById(@PathVariable Long id) {
         IssuanceDTO issuanceDTO =  iIssuanceService.getIssuanceById(id);
         return  ResponseEntity.status(HttpStatus.OK).body(issuanceDTO);
    }

    @CrossOrigin
    @PostMapping("/save")
    public  ResponseEntity<String> createIssuance(@Valid @RequestBody IssuanceDTO issuanceDTO) {
         String response = iIssuanceService.createIssuance(issuanceDTO);
         return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @CrossOrigin
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateIssuance(@PathVariable Long id, @Valid @RequestBody IssuanceDTO issuanceDTO) {
        String response = iIssuanceService.updateIssuance(id, issuanceDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIssuance(@PathVariable Long id) {
        String response = iIssuanceService.deleteIssuance(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/userHistory")
    public  String userHistory() {
        return  "User History only Acceccible by User";
    }

   @CrossOrigin
   @GetMapping("/list")
   public ResponseEntity<Page<IssuanceOutDTO>> getAllIssuances(
           @RequestParam(value = "page", defaultValue = "0") int page,
           @RequestParam(value = "size", defaultValue = "10") int size,
           @RequestParam(value = "search", required = false) String search
   ) {

        Page<IssuanceOutDTO> issuanceOutDTO =iIssuanceService.getIssuanceList(page, size, search);
       return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
   }


    @CrossOrigin
    @GetMapping("/count")
    public ResponseEntity<Long> getIssuanceCount() {
        Long count = iIssuanceService.getIssuanceCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }


    @CrossOrigin
    @GetMapping("/userIssuanceDetails")
    public  ResponseEntity<Page<UserHistoryOutDTO>> getIssuanceDetailsByUserId(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {


        Page<UserHistoryOutDTO> userHistoryOutDTOS = iIssuanceService.getIssuanceDetailsByUserId(userId, page, size);

        return  ResponseEntity.status(HttpStatus.OK).body(userHistoryOutDTOS);
    }

    @CrossOrigin
    @GetMapping("/bookIssuanceDetails")
    public  ResponseEntity<Page<BookHistoryOutDTO>> getIssuanceDetailsByBookId(
            @RequestParam(value = "bookId") Long bookId,
            @RequestParam(value = "page",defaultValue = "0") int page ,
            @RequestParam(value = "size" ,defaultValue = "10") int size
    ) {
        Page<BookHistoryOutDTO> bookHistoryOutDTOS = iIssuanceService.getIssuanceDetailsByBookId(bookId,page,size);
        return  ResponseEntity.status(HttpStatus.OK).body(bookHistoryOutDTOS);
    }

}
