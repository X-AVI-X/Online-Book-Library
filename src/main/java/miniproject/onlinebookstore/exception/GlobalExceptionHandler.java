package miniproject.onlinebookstore.exception;

import miniproject.onlinebookstore.dto.ActionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ActionResponse> handleIdNotFoundException(IdNotFoundException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ActionResponse> handleBookNotAvailableException(BookNotAvailableException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoReservationException.class)
    public ResponseEntity<ActionResponse> handleNoReservation(NoReservationException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ActionResponse> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.CONFLICT);
    }


}

