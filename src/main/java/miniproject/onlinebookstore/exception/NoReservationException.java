package miniproject.onlinebookstore.exception;

import lombok.*;
import miniproject.onlinebookstore.dto.ActionResponse;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class NoReservationException extends Exception{
    private ActionResponse errorResponse;
    public NoReservationException(String message){
        errorResponse = new ActionResponse(message);
    }
}
