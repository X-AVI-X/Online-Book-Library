package miniproject.onlinebookstore.exception;

import lombok.*;
import miniproject.onlinebookstore.dto.ActionResponse;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomException extends Exception{
    private ActionResponse errorResponse;
    public CustomException(String message){
        errorResponse = new ActionResponse(message);
    }
}
