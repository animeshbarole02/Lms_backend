package com.nucleusTeq.backend.controllers;


import com.nucleusTeq.backend.dto.IssuanceDTO;
import com.nucleusTeq.backend.services.IIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/save")
    public  ResponseEntity<String> createIssuance(@Valid @RequestBody IssuanceDTO issuanceDTO) {
         String response = iIssuanceService.createIssuance(issuanceDTO);
         return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIssuance(@PathVariable Long id, @Valid @RequestBody IssuanceDTO issuanceDTO) {
        String response = iIssuanceService.updateIssuance(id, issuanceDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIssuance(@PathVariable Long id) {
        String response = iIssuanceService.deleteIssuance(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/userHistory")
    public  String userHistory() {
        return  "User History only Acceccible by User";
    }

}
