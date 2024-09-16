package com.nucleusTeq.backend.controllers;

import com.nucleusTeq.backend.services.IBooksService;
import com.nucleusTeq.backend.services.ICategoryService;
import com.nucleusTeq.backend.services.IIssuanceService;
import com.nucleusTeq.backend.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "api/v1/dashboard")
public class DashboardController {


    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IBooksService iBooksService;
    @Autowired
    private IUsersService iUsersService;
    @Autowired
    private IIssuanceService iIssuanceService;


    @CrossOrigin
    @GetMapping("/count")
    public ResponseEntity<Map<String,Object>> getAllCounts() {

        Map<String ,Object> counts  = new HashMap<>();

        Long bookCount = iBooksService.getTotalBookCount();
        Long categoryCount = iCategoryService.getCategoryCount();
        Long userCount = iUsersService.getUserCount();
        Long issuanceCount = iIssuanceService.getIssuanceCount();

        counts.put("bookCount",bookCount);
        counts.put("categoryCount",categoryCount);
        counts.put("userCount",userCount);
        counts.put("issuanceCount",issuanceCount);

        return  ResponseEntity.status(HttpStatus.OK).body(counts);

    }
}
