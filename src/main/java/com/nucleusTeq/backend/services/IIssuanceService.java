package com.nucleusTeq.backend.services;

import com.nucleusTeq.backend.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IIssuanceService {

    List<IssuanceDTO> getAllIssuances();
    IssuanceDTO getIssuanceById(Long id);
    String createIssuance(IssuanceDTO issuanceDTO);
    String updateIssuance(Long id , IssuanceDTO issuanceDTO);
    String deleteIssuance(Long id);
    Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search);
    Long getIssuanceCount();

    Page<UserHistoryOutDTO> getIssuanceDetailsByUserId(Long userId, int page, int size);
    Page<BookHistoryOutDTO> getIssuanceDetailsByBookId(Long bookId , int page , int size);

}
