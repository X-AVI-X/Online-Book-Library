package miniproject.onlinebookstore.repository;

import miniproject.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
