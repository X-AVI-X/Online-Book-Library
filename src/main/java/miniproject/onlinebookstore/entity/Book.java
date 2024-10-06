package miniproject.onlinebookstore.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;
    private Double overallRating;
    private int publicationYear;

    @Enumerated(EnumType.STRING)
    private BookStatus status; // AVAILABLE, BORROWED, RESERVED}

    private boolean deleted = false;
}
