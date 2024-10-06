package miniproject.onlinebookstore.service;

import miniproject.onlinebookstore.dto.ActionResponse;
import miniproject.onlinebookstore.dto.BookReviewDto;
import miniproject.onlinebookstore.dto.BookReviewResponse;
import miniproject.onlinebookstore.entity.BookReview;
import miniproject.onlinebookstore.exception.BookNotAvailableException;
import miniproject.onlinebookstore.exception.IdNotFoundException;
import miniproject.onlinebookstore.repository.BookRepository;
import miniproject.onlinebookstore.repository.BookReviewRepository;
import miniproject.onlinebookstore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookReviewService {
    private final BookReviewRepository repository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public BookReviewService(BookReviewRepository repository, BookRepository bookRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public BookReviewResponse createReview (Long bookId, BookReviewDto bookReviewDto) throws BookNotAvailableException, IdNotFoundException {
        BookReview bookReview = null;
        if (bookRepository.existsById(bookId)){
            if (userRepository.existsById(bookReviewDto.getUserId())){
                if (repository.findByBookIdAndUserId(bookId, bookReviewDto.getUserId()) == null){
                    bookReview = modelMapper.map(bookReviewDto, BookReview.class);
                    bookReview.setBook(bookRepository.findById(bookId).get());
                    bookReview.setUser(userRepository.findById(bookReviewDto.getUserId()).get());
                    double overallRating = bookReview.getRating();
                    int totalReview =1;
                    for (BookReview bookReview1 : repository.findByBookId(bookId)){
                        overallRating+=bookReview1.getRating();
                        totalReview++;
                    }
                    if (totalReview!=0)  overallRating = overallRating/totalReview;
                    bookReview.getBook().setOverallRating(overallRating);
                    bookReview = repository.save(bookReview);
                }else throw new BookNotAvailableException("You have already reviewed.");
            } else throw new IdNotFoundException("User not found.");
        }else throw new IdNotFoundException("Book not found.");
        return modelMapper.map(bookReview, BookReviewResponse.class);
    }

    public List<BookReviewResponse> getAll (Long bookId) throws BookNotAvailableException {
        List<BookReview> bookReviews = repository.findByBookId(bookId);
        List<BookReviewResponse> bookReviewResponses = new ArrayList<>();
        for (BookReview bookReview : bookReviews){
            bookReviewResponses.add(modelMapper.map(bookReview, BookReviewResponse.class));
        }
        if (bookReviewResponses.isEmpty()){
            throw new BookNotAvailableException("No review found.");
        }
        else return bookReviewResponses;
    }

    public BookReviewResponse update (Long bookId, Long reviewId, BookReviewDto bookReviewDto) throws IdNotFoundException {
        BookReview bookReview = repository.findById(reviewId).orElseThrow(()->new IdNotFoundException("Id not found"));
        bookReview.setReviewText(bookReviewDto.getReviewText());
        bookReview.setRating(bookReviewDto.getRating());
        bookReview = repository.save(bookReview);
        return modelMapper.map(bookReview, BookReviewResponse.class);
    }

    public ActionResponse delete (Long reviewId) throws IdNotFoundException {
        if (repository.existsById(reviewId)){
            repository.deleteById(reviewId);
            return new ActionResponse("Deleted");
        }else throw new IdNotFoundException("Review doesn't exists");
    }
}
