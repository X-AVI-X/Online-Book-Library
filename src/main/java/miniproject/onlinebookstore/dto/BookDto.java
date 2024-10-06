package miniproject.onlinebookstore.dto;

import lombok.*;
import miniproject.onlinebookstore.entity.BookStatus;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookDto {
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    private String genre;

    private int publicationYear;
    private BookStatus status;
}
