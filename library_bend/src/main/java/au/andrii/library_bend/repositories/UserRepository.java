package au.andrii.library_bend.repositories;

import au.andrii.library_bend.models.Book;
import au.andrii.library_bend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT b FROM Book b WHERE b.user.id = :userId")
    List<Book> findBookListById(int userId);
    List<User> findUsersBySurnameContaining(String searchingFragment);
    List<User> findUsersByNameContaining (String searchingFragment);
    List<User> findUsersByEmailContaining(String searchingFragment);
}
