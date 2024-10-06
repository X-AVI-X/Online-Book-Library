package miniproject.onlinebookstore.repository;

import miniproject.onlinebookstore.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long>{

    @Query("SELECT uh FROM UserHistory uh WHERE uh.book.id = :bookId AND uh.isCurrentlyBorrowed = 'YES'")
    UserHistory findCurrentlyBorrowedHistory(@Param("bookId") Long bookId);

    List<UserHistory> findUserHistoriesByBookIdAndUserId(Long bookId, Long userId);

    @Query("SELECT uh FROM UserHistory uh WHERE uh.book.id = :bookId AND uh.isReserved = 'YES' AND uh.user.id=:userId")
    UserHistory findReservedUserHistoriesByBookIdAndUserId(Long bookId, Long userId);


    List<UserHistory> findByUserId(Long userId);

    @Query("SELECT uh FROM UserHistory uh WHERE uh.user.id = :userId AND uh.isCurrentlyBorrowed = 'NO'")
    List<UserHistory> findPreviouslyBorrowedByUserId (@Param("userId") Long userId);

    @Query("SELECT uh FROM UserHistory uh WHERE uh.user.id = :userId AND uh.isCurrentlyBorrowed = 'YES'")
    List<UserHistory> findCurrentlyBorrowedByUserId (@Param("userId") Long userId);
}
