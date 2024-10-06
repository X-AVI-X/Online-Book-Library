package miniproject.onlinebookstore.service;

import miniproject.onlinebookstore.dto.HistoryResponse;
import miniproject.onlinebookstore.dto.UserDto;
import miniproject.onlinebookstore.entity.*;
import miniproject.onlinebookstore.exception.BookNotAvailableException;
import miniproject.onlinebookstore.exception.IdNotFoundException;
import miniproject.onlinebookstore.exception.NoReservationException;
import miniproject.onlinebookstore.repository.BookRepository;
import miniproject.onlinebookstore.repository.UserHistoryRepository;
import miniproject.onlinebookstore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookOperationService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final ModelMapper modelMapper;

    public BookOperationService(BookRepository bookRepository,
                                UserRepository userRepository,
                                UserHistoryRepository userHistoryRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userHistoryRepository = userHistoryRepository;
        this.modelMapper = modelMapper;
    }

    public UserHistory borrowBook(Long bookId, Long userId) throws IdNotFoundException, BookNotAvailableException {
        UserHistory borrow = new UserHistory();
        LocalDate currentDate = LocalDate.now();
        LocalDate date5daysAfter = currentDate.plusDays(5);
        borrow.setBorrowDate(currentDate);
        borrow.setReturnDate(date5daysAfter);
        Book book = bookRepository.findById(bookId).orElseThrow(()->new IdNotFoundException("Book not found"));

        if (book.getStatus() == BookStatus.AVAILABLE) {
            if (userRepository.existsById(userId)) {
                borrow.setUser(userRepository.findById(userId).get());
                borrow.setBook(book);
                borrow.getBook().setStatus(BookStatus.BORROWED);
                }
            else throw new IdNotFoundException("User not found.");
            }
        else throw new BookNotAvailableException("Book is not available to borrow.");
        borrow.setIsCurrentlyBorrowed(IsCurrentlyBorrowed.YES);
        borrow = userHistoryRepository.save(borrow);
        borrow.getUser().setPassword(null);

        return borrow;
    }

    public UserHistory returnBook (Long bookId) throws IdNotFoundException, BookNotAvailableException {
        Book book = bookRepository.findById(bookId).orElseThrow(()->new IdNotFoundException("Book not found"));
        UserHistory userHistory = null;
        if (book.getStatus()==BookStatus.BORROWED){
            userHistory = userHistoryRepository.findCurrentlyBorrowedHistory(bookId);
            userHistory.setIsCurrentlyBorrowed(IsCurrentlyBorrowed.NO);
            userHistory.getBook().setStatus(BookStatus.AVAILABLE);
            userHistory = userHistoryRepository.save(userHistory);

            userHistory.getUser().setPassword(null);

        } else throw new BookNotAvailableException("Book is not borrowed to be returned.");
        return userHistory;
    }

    public UserHistory reserve (Long bookId, Long userId) throws IdNotFoundException, BookNotAvailableException, NoReservationException {
        UserHistory reservation = new UserHistory();
        reservation.setReservationDate(LocalDate.now());
        reservation.setIsReserved(IsReserved.YES);
        Book book = bookRepository.findById(bookId).orElseThrow(()->new IdNotFoundException("Book not found"));
        Boolean alreadyReservedFlag = false;
        if (book.getStatus() == BookStatus.BORROWED) {
            if (userRepository.existsById(userId)) {
                List<UserHistory> reservationHistory = userHistoryRepository.findUserHistoriesByBookIdAndUserId(bookId, userId);
                for (UserHistory pastReservation : reservationHistory){
                    if (pastReservation.getIsReserved() == IsReserved.YES){
                        throw new NoReservationException("Already reserved");
                    }
                    if(userId.equals(userHistoryRepository.findCurrentlyBorrowedHistory(bookId).getUser().getId())){
                        throw new NoReservationException("You have already borrowed this book");
                    }
                }

                reservation.setIsReserved(IsReserved.YES);
                reservation.setUser(userRepository.findById(userId).get());
                reservation.setBook(book);
            }
            else throw new IdNotFoundException("User not found.");
        }
        else throw new BookNotAvailableException("Book is not available to reserve.");
        reservation.setIsReservationCancelled(IsReservationCancelled.NO);
        reservation = userHistoryRepository.save(reservation);
        reservation.getUser().setPassword(null);

        return reservation;
    }

    public UserHistory cancelReservation (Long bookId, Long userId) throws BookNotAvailableException, IdNotFoundException, NoReservationException {
        UserHistory cancelReservation;
        if (userRepository.existsById(userId)){
            if (bookRepository.existsById(bookId)){
                cancelReservation = userHistoryRepository.findReservedUserHistoriesByBookIdAndUserId(bookId, userId);
                if(cancelReservation!=null) {
                    cancelReservation.setIsReservationCancelled(IsReservationCancelled.YES);
                    cancelReservation.setIsReserved(IsReserved.NO);
                    cancelReservation = userHistoryRepository.save(cancelReservation);
                    cancelReservation.getUser().setPassword(null);
                } else throw new NoReservationException("Can not cancel reservation.");
            }else throw new BookNotAvailableException("Book is available.");
        }else throw new IdNotFoundException("User not found.");
        return cancelReservation;
    }


    public List<HistoryResponse> getHistoryByUserId(Long userId) {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        for (UserHistory userHistory : userHistoryRepository.findByUserId(userId)){
            HistoryResponse historyResponse = modelMapper.map(userHistory, HistoryResponse.class);
            historyResponse.setUserDto(modelMapper.map(userHistory.getUser(), UserDto.class));
            historyResponses.add(historyResponse);
        }
        return historyResponses;
    }
}
