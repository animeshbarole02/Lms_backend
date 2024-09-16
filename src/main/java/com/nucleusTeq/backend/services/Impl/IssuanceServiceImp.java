package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.dto.BookHistoryOutDTO;
import com.nucleusTeq.backend.dto.IssuanceDTO;
import com.nucleusTeq.backend.dto.IssuanceOutDTO;
import com.nucleusTeq.backend.dto.UserHistoryOutDTO;
import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Issuance;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.exception.MethodNotFoundException;
import com.nucleusTeq.backend.exception.ResourceNotFoundException;
import com.nucleusTeq.backend.mapper.HistoryMapper;
import com.nucleusTeq.backend.mapper.IssuanceMapper;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.CategoryRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.IIssuanceService;
import com.nucleusTeq.backend.services.ISMSService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssuanceServiceImp implements IIssuanceService {


    private final IssuanceRepository issuanceRepository;
    private  final CategoryRepository categoryRepository;
    private final BooksRepository booksRepository;
    private final UsersRepository usersRepository;

    @Autowired
    private ISMSService ismsService;

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



        Issuance issuance = IssuanceMapper.mapToIssuance(issuanceDTO);


        Optional<Books> bookOpt = booksRepository.findById(issuanceDTO.getBookId());
        if (bookOpt.isPresent()) {
            Books book = bookOpt.get();
            if (book.getQuantity() > 0) {

                book.setQuantity(book.getQuantity() - 1);
                booksRepository.save(book);




                Issuance savedIssuance =  issuanceRepository.save(issuance);

                Books booktoSend = booksRepository.findById(savedIssuance.getBookId())
                        .orElseThrow(() -> new RuntimeException("Book not found for ID: " + savedIssuance.getBookId()));

                Users user = usersRepository.findById(savedIssuance.getUserId())
                        .orElseThrow(()->new RuntimeException("User Id is not found:"+ savedIssuance.getUserId()));

                String message = String.format("\nYou have issued the book '%s'\n" +
                                "author '%s'\n" +
                                "From %s\n" +
                                "To %s",


                        booktoSend.getTitle(),
                        booktoSend.getAuthor(),
                        savedIssuance.getIssuedAt().toLocalDate(),
                        savedIssuance.getExpectedReturn().toLocalDate());

             //   ismsService.sendSms(user.getPhoneNumber(), message);
                return "Issuance Added Successfully";
            } else {
                throw  new ResourceNotFoundException("No copies available for the selected book.");
            }
        } else {
            throw new ResourceNotFoundException( "Book not found.");
        }





    }

    @Override
    public String updateIssuance(Long id, IssuanceDTO issuanceDTO) {
        Optional<Issuance> optionalIssuance = issuanceRepository.findById(id);

        if (optionalIssuance.isPresent()) {
            Issuance existingIssuance = optionalIssuance.get();

            String originalStatus =  existingIssuance.getStatus();




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
                    throw  new ResourceNotFoundException("Book not found");
                }

            }


            issuanceRepository.save(existingIssuance);
            return "Issuance updated successfully ";
        } else {
            throw new ResourceNotFoundException("Issuance not found ");
        }
    }

    @Override
    public String deleteIssuance(Long id) {
        // Find the issuance by ID
        Issuance issuance = issuanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issuance not found " ));

        Optional<Books> bookOpt = booksRepository.findById(issuance.getBookId());
        if (bookOpt.isPresent()) {
            Books book = bookOpt.get();


            book.setQuantity(book.getQuantity() + 1);
            booksRepository.save(book);
        } else {
            throw new ResourceNotFoundException("Book not found ");
        }

        issuanceRepository.delete(issuance);

        return "Issuance deleted successfully ";
    }

    @Override
    public Page<IssuanceOutDTO> getIssuanceList(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt"));


        Page<Issuance> issuances;
        if (search == null || search.isEmpty()) {
            issuances = issuanceRepository.findAll(pageRequest);
        } else {
            issuances = issuanceRepository.findByBookTitleOrUserName(search, pageRequest);
        }

        // Map issuances to IssuanceOutDTO
        return issuances.map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, booksRepository, usersRepository));
    }





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
