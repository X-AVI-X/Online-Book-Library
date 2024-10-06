package miniproject.onlinebookstore.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookReviewDto {
    private String reviewText;
    private int rating;
    private Long userId;
}
