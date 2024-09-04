package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.dto.BookHistoryOutDTO;
import com.nucleusTeq.backend.dto.IssuanceDTO;
import com.nucleusTeq.backend.dto.IssuanceOutDTO;
import com.nucleusTeq.backend.dto.UserHistoryOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Issuance;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.HistoryMapper;
import com.nucleusTeq.backend.mapper.IssuanceMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.IIssuanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IssuanceServiceImp implements IIssuanceService {

    @Autowired
    private final IssuanceRepository issuanceRepository;

    @Autowired
    private  final CategoryRepository categoryRepository;

    @Autowired
    private final BooksRepository booksRepository;

    @Autowired
    private final UsersRepository usersRepository;

    @Override
    public List<IssuanceDTO> getAllIssuances() {
        List<Issuance> issuances = issuanceRepository.findAll();
        List<IssuanceDTO> issuanceDTOList = new ArrayList<>();

        issuances.forEach(issuance -> {
            issuanceDTOList.add(IssuanceMapper.mapToIssuanceDTO(issuance));
        });

        return issuanceDTOList;
    }

    @Override
    public IssuanceDTO getIssuanceById(Long id) {
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issuance not found with ID : " + id));

        return IssuanceMapper.mapToIssuanceDTO(issuance);
    }

    @Override
    public String createIssuance(IssuanceDTO issuanceDTO) {
        // Check if an issuance already exists for the given user and book
        // Check if an issuance already exists for the given user and book
        Optional<Issuance> existingIssuance = issuanceRepository.findByUserIdAndBookId(issuanceDTO.getUserId(), issuanceDTO.getBookId());

        // If an issuance already exists, return a message
        if (existingIssuance.isPresent()) {
            return "Issuance already exists for this user and book.";
        }

        // Map DTO to Entity
        Issuance issuance = IssuanceMapper.mapToIssuance(issuanceDTO);

        // Retrieve the book and check the quantity
        Optional<Books> bookOpt = booksRepository.findById(issuanceDTO.getBookId());
        if (bookOpt.isPresent()) {
            Books book = bookOpt.get();
            if (book.getQuantity() > 0) {
                // Decrease the book count
                book.setQuantity(book.getQuantity() - 1);
                booksRepository.save(book); // Save the updated book

                // Save the issuance
                issuanceRepository.save(issuance);
                return "Issuance Added Successfully with ID: " + issuance.getId();
            } else {
                return "No copies available for the selected book.";
            }
        } else {
            return "Book not found.";
        }

    }

    @Override
    public String updateIssuance(Long id, IssuanceDTO issuanceDTO) {
        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);

        if (optionalIssuance.isPresent()) {
            Issuance existingIssuance = optionalIssuance.get();

            String originalStatus =  existingIssuance.getStatus();



            // Update only the specified fields if they are not null
            if (issuanceDTO.getExpectedReturn() != null) {
                existingIssuance.setExpectedReturn(issuanceDTO.getExpectedReturn());
            }
            if (issuanceDTO.getStatus() != null) {
                existingIssuance.setStatus(issuanceDTO.getStatus());
            }

            if("Returned".equalsIgnoreCase(issuanceDTO.getStatus()) &&
               !"Returned".equalsIgnoreCase(originalStatus)){

                Optional<Books> bookopt = booksRepository.findById(existingIssuance.getBookId());

                if(bookopt.isPresent()) {
                    Books book = bookopt.get();
                    book.setQuantity(book.getQuantity() +1 );
                    booksRepository.save(book);
                }else {
                    throw  new ResourceNotFoundException("Book not found with ID :" + existingIssuance.getBookId());
                }

            }


            issuanceRepository.save(existingIssuance);
            return "Issuance updated successfully with ID: " + id;
        } else {
            throw new ResourceNotFoundException("Issuance not found with ID: " + id);
        }
    }

    @Override
    public String deleteIssuance(Long id) {
        // Find the issuance by ID
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issuance not found with ID: " + id));

        // Retrieve the book associated with this issuance

        // Delete the issuance
        issuanceRepository.delete(issuance);

        return "Issuance deleted successfully with ID: " + id;
    }

    @Override
    public Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Issuance> issuances = issuanceRepository.findAll(pageRequest); // Implement search logic if needed

        return issuances.map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, booksRepository, usersRepository));
    }

    // New method to check if a user has an active issuance


    @Override
    public Long getIssuanceCount() {
        return issuanceRepository.count();
    }


    @Override
    public Page<UserHistoryOutDTO> getIssuanceDetailsByUserId(Long userId, int page, int size) {
        // Create a PageRequest object to define the pagination settings

        PageRequest pageRequest = PageRequest.of(page, size);


        Page<Issuance> issuances = issuanceRepository.findByUserId(userId, pageRequest);

        // Map each Issuance entity to a UserHistoryOutDTO
        return issuances.map(issuance -> HistoryMapper.mapToUserHistoryOutDTO(issuance, booksRepository, categoryRepository));



    }

    @Override
    public Page<BookHistoryOutDTO> getIssuanceDetailsByBookId(Long bookId , int page ,  int size) {

        PageRequest pageRequest = PageRequest.of(page,size);

        Page<Issuance> issuances = issuanceRepository.findByBookId(bookId ,pageRequest);

        System.out.println(issuances);

        return  issuances.map(issuance -> HistoryMapper.mapToBookHistoryOutDTO(issuance,usersRepository));
    }
}
