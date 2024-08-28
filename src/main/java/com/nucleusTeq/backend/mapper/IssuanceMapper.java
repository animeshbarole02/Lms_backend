package com.nucleusTeq.backend.mapper;

import com.nucleusTeq.backend.dto.IssuanceDTO;
import com.nucleusTeq.backend.entities.Issuance;

public class IssuanceMapper {

    private  IssuanceMapper(){

    }

    public  static IssuanceDTO mapToIssuanceDTO(Issuance issuance) {

        IssuanceDTO issuanceDTO  =  new IssuanceDTO();
        issuanceDTO.setId(issuance.getId());
        issuanceDTO.setUserId(issuance.getUserId());
        issuanceDTO.setBookId(issuance.getBookId());
        issuanceDTO.setIssueAt(issuance.getIssuedAt());
        issuanceDTO.setReturnedAt(issuance.getReturnedAt());
        issuanceDTO.setStatus(issuance.getStatus());
        issuanceDTO.setIssuanceType(issuance.getIssuanceType());


        return  issuanceDTO;


    }

    public  static Issuance maptoIssuance(IssuanceDTO issuanceDTO)
    {
        Issuance issuance =  new Issuance();
        issuance.setId(issuanceDTO.getId());
        issuance.setUserId(issuanceDTO.getUserId());
        issuance.setBookId(issuanceDTO.getBookId());
        issuance.setIssuedAt(issuanceDTO.getIssueAt());
        issuance.setReturnedAt(issuanceDTO.getReturnedAt());
        issuance.setStatus(issuanceDTO.getStatus());
        issuance.setIssuanceType(issuanceDTO.getIssuanceType());

        return  issuance;



    }

}
