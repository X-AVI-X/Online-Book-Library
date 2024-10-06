package miniproject.onlinebookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import miniproject.onlinebookstore.entity.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryResponse {
    private Long id;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate reservationDate;
    private UserDto userDto;
    private Book book;
    private IsCurrentlyBorrowed isCurrentlyBorrowed;
    private IsReserved isReserved;
    private IsReservationCancelled isReservationCancelled;
}
