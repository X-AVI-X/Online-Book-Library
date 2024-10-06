package miniproject.onlinebookstore.exception;

import lombok.*;
import miniproject.onlinebookstore.dto.ActionResponse;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class BookNotAvailableException extends Exception{

    private ActionResponse errorResponse;
    public BookNotAvailableException(String message){
        errorResponse = new ActionResponse(message);
    }
}
