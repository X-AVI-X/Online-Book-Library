package miniproject.onlinebookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewText;
    @Min(value = 0, message = "Can not be lower than 1")
    @Max(value = 10, message = "Can not be higher than 10")
    private int rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private boolean deleted = false;

}
