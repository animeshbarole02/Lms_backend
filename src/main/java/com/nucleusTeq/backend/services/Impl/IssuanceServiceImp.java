package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.dto.IssuanceDTO;
import com.nucleusTeq.backend.entities.Issuance;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.IssuanceMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.IIssuanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IssuanceServiceImp implements IIssuanceService {

    @Autowired
    private  final IssuanceRepository issuanceRepository;

    @Autowired
    private  final BooksRepository booksRepository;

    @Autowired
    private  final UsersRepository usersRepository;


    @Override
    public List<IssuanceDTO> getAllIssuances() {
        List<Issuance> issuances = issuanceRepository.findAll();
        List<IssuanceDTO> issuanceDTOList =  new ArrayList<>();

        issuances.forEach(issuance -> {
            issuanceDTOList.add(IssuanceMapper.mapToIssuanceDTO(issuance));
        });

        return issuanceDTOList;
    }

    @Override
    public IssuanceDTO getIssuanceById(Long id) {
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Issuance not found with ID : " + id));

        return  IssuanceMapper.mapToIssuanceDTO(issuance);
    }

    @Override
    public String createIssuance(IssuanceDTO issuanceDTO) {

        Issuance issuance = IssuanceMapper.maptoIssuance(issuanceDTO);
        issuanceRepository.save(issuance);
        return "Issuance Added Successfully with ID : " + issuance.getId();
    }

    @Override
    public String updateIssuance(Long id, IssuanceDTO issuanceDTO) {

        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);

        if(optionalIssuance.isPresent()){

            Issuance existingIssuance = optionalIssuance.get();
            existingIssuance.setBookId(issuanceDTO.getBookId());
            existingIssuance.setUserId(issuanceDTO.getUserId());
            existingIssuance.setIssuedAt(issuanceDTO.getIssueAt());
            existingIssuance.setReturnedAt(issuanceDTO.getReturnedAt());
            existingIssuance.setIssuanceType(issuanceDTO.getIssuanceType());
            existingIssuance.setStatus(issuanceDTO.getStatus());

            issuanceRepository.save(existingIssuance);
            return  "Issuance updated successfully with ID: " + id;
        }else {
            throw  new ResourceNotFoundException("Issuance not fount with ID : "+ id);
        }

    }

    @Override
    public String deleteIssuance(Long id) {

        Issuance issuance =  issuanceRepository.findById(id)
                .orElseThrow(()->
                    new ResourceNotFoundException("Issuance not found with ID: " + id));
          issuanceRepository.delete(issuance);

        return "Issuance deleted successfully with ID: " + id;
    }
}
