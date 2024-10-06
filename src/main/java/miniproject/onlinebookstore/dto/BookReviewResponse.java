package miniproject.onlinebookstore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookReviewResponse {
    private Long id;
    private String reviewText;
    private int rating;
}
