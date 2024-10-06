package miniproject.onlinebookstore.exception;

import lombok.*;
import miniproject.onlinebookstore.dto.ActionResponse;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class IdNotFoundException extends Exception{

    private ActionResponse errorResponse;
    public IdNotFoundException(String message){
        errorResponse = new ActionResponse(message);
    }
}
