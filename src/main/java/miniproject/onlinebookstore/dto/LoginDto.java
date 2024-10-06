package miniproject.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor

public class LoginDto {
    @NonNull
    private String email;

    private String password;
}
