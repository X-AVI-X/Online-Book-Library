package miniproject.onlinebookstore.repository;

import miniproject.onlinebookstore.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    BookReview findByBookIdAndUserId (Long bookId, Long userId);
    List<BookReview> findByBookId(Long bookId);
}
