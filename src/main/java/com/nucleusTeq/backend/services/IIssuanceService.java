package com.nucleusTeq.backend.services;

import com.nucleusTeq.backend.dto.IssuanceDTO;

import java.util.List;

public interface IIssuanceService {

    List<IssuanceDTO> getAllIssuances();
    IssuanceDTO getIssuanceById(Long id);
    String createIssuance(IssuanceDTO issuanceDTO);
    String updateIssuance(Long id , IssuanceDTO issuanceDTO);
    String deleteIssuance(Long id);

}
