package com.nucleusTeq.backend.controllers;

import com.nucleusTeq.backend.dto.UserHistoryOutDTO;
import com.nucleusTeq.backend.services.IIssuanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/v1/user")
public class HistoryController {



    @Autowired
    private IIssuanceService iIssuanceService;

    @CrossOrigin
    @GetMapping("/userIssuanceDetails")
    public ResponseEntity<Page<UserHistoryOutDTO>> getIssuanceDetailsByUserId(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {


        Page<UserHistoryOutDTO> userHistoryOutDTOS = iIssuanceService.getIssuanceDetailsByUserId(userId, page, size);

        return  ResponseEntity.status(HttpStatus.OK).body(userHistoryOutDTOS);
    }
}
