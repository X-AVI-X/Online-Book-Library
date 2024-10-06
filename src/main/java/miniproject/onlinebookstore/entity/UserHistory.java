package miniproject.onlinebookstore.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate reservationDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;
    @Enumerated(EnumType.STRING)
    private IsCurrentlyBorrowed isCurrentlyBorrowed;

    @Enumerated(EnumType.STRING)
    private IsReserved isReserved;

    @Enumerated(EnumType.STRING)
    private IsReservationCancelled isReservationCancelled;

}
