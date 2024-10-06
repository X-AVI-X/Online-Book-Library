package miniproject.onlinebookstore.controller;

import miniproject.onlinebookstore.dto.BookDto;
import miniproject.onlinebookstore.dto.BookReviewDto;
import miniproject.onlinebookstore.entity.UserHistory;
import miniproject.onlinebookstore.exception.BookNotAvailableException;
import miniproject.onlinebookstore.exception.IdNotFoundException;
import miniproject.onlinebookstore.exception.NoReservationException;
import miniproject.onlinebookstore.service.BookOperationService;
import miniproject.onlinebookstore.service.BookReviewService;
import miniproject.onlinebookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
public class BookController {
    private final BookService service;
    private final BookOperationService bookOperationService;
    private final BookReviewService bookReviewService;

    public BookController(BookService service, BookOperationService bookOperationService, BookReviewService bookReviewService) {
        this.service = service;
        this.bookOperationService = bookOperationService;
        this.bookReviewService = bookReviewService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create (@RequestBody BookDto bookDto){
        return ResponseEntity.ok(service.createBook(bookDto));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> update (@RequestBody BookDto bookDto) throws Exception {
        return ResponseEntity.ok(service.update(bookDto));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete (@RequestParam Long id){
        return ResponseEntity.ok(service.delete(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll (){
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<UserHistory> borrow (@PathVariable Long bookId, @RequestParam Long userId) throws BookNotAvailableException, IdNotFoundException {
        return new ResponseEntity<>(bookOperationService.borrowBook(bookId, userId), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PutMapping("/{bookId}/return")
    public ResponseEntity<UserHistory> returnBook (@PathVariable Long bookId) throws BookNotAvailableException, IdNotFoundException {
        return new ResponseEntity<>(bookOperationService.returnBook(bookId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/{bookId}/reserve")
    public ResponseEntity<UserHistory> reserve (@PathVariable Long bookId, @RequestParam Long userId) throws BookNotAvailableException, IdNotFoundException, NoReservationException {
        return new ResponseEntity<>(bookOperationService.reserve(bookId, userId), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PutMapping("/{bookId}/cancel-reservation")
    public ResponseEntity<UserHistory> cancelReservation (@PathVariable Long bookId, @RequestParam Long userId) throws BookNotAvailableException, IdNotFoundException, NoReservationException {
        return new ResponseEntity<>(bookOperationService.cancelReservation(bookId, userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("{bookId}/reviews/create")
    public ResponseEntity<?> createReview (@RequestBody BookReviewDto bookReviewDto, @PathVariable Long bookId) throws BookNotAvailableException, IdNotFoundException {
        return new ResponseEntity<>(bookReviewService.createReview(bookId, bookReviewDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    @GetMapping("{bookId}/reviews")
    public ResponseEntity<?> getAllReviewByBookId(@PathVariable Long bookId) throws BookNotAvailableException {
        return ResponseEntity.ok(bookReviewService.getAll(bookId));
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PutMapping("{bookId}/reviews/{reviewId}/update")
    public ResponseEntity<?> updateReview (@PathVariable Long bookId, @PathVariable Long reviewId, @RequestBody BookReviewDto reviewDto) throws IdNotFoundException {
        return ResponseEntity.ok(bookReviewService.update(bookId, reviewId, reviewDto));
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @DeleteMapping("{bookId}/reviews/{reviewId}/delete")
    public ResponseEntity<?> deleteReview (@PathVariable Long bookId, @PathVariable Long reviewId) throws IdNotFoundException {
        return new ResponseEntity<>(bookReviewService.delete(reviewId), HttpStatus.OK);
    }

}
