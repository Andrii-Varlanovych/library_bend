package au.andrii.library_bend.repositories;

import au.andrii.library_bend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByTitleContaining(String str);
    List<Book> findBooksByAuthorContaining(String str);
    List<Book> findBooksByIsAvailable(Boolean bool);
    List<Book> findBookByTitleStartingWith(String str);
}
