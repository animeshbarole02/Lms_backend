package com.nucleusTeq.backend.services.Impl;

import com.nucleusTeq.backend.entities.Books;
import com.nucleusTeq.backend.entities.Issuance;
import com.nucleusTeq.backend.entities.Users;
import com.nucleusTeq.backend.repositories.BooksRepository;
import com.nucleusTeq.backend.repositories.IssuanceRepository;
import com.nucleusTeq.backend.repositories.UsersRepository;
import com.nucleusTeq.backend.services.IBookReturnReminderService;
import com.nucleusTeq.backend.services.ISMSService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BookReturnReminderService implements IBookReturnReminderService {


    private final  BooksRepository booksRepository;
    private final IssuanceRepository issuanceRepository;
    private final UsersRepository usersRepository;
    private final  ISMSService ismsService;
    //    second minute hour day-of-month month day-of-week
//    @Scheduled(cron = "0 0 15 * * *", zone = "Asia/Kolkata")

    @Override
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void sendReturnRemainder() {
        LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1);
        List<Issuance> dueTomorrow = issuanceRepository.findAllByExpectedReturnTimeBetweenAndStatus(startOfTomorrow, endOfTomorrow, "Issued");

        System.out.println("SCHEDULER CALLED" + dueTomorrow);

        for (Issuance issuance : dueTomorrow) {

            Optional<Books> bookOptional = booksRepository.findById(issuance.getBookId());
            Optional<Users> userOptional = usersRepository.findById(issuance.getUserId());

            if (bookOptional.isPresent() &&  userOptional.isPresent()) {
                Books book = bookOptional.get();
                Users user = userOptional.get();
                String message = String.format("\nReminder:\n" +
                                "Please return the book '%s'\n" +
                                "Author '%s'\n" +
                                "by tomorrow (%s).",
                        book.getTitle(), book.getAuthor(), issuance.getExpectedReturn().toLocalDate());

                ismsService.sendSms(user.getPhoneNumber(), message);
                System.out.println(dueTomorrow);
            } else {
                System.out.println("Book not found for Issuance ID: " + issuance.getId());
            }
        }
    }

}
