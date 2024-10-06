package miniproject.onlinebookstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReservedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservedBookId;

    private boolean notificationSent;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;
}
