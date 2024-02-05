package au.andrii.library_bend.services;

import au.andrii.library_bend.models.Book;
import au.andrii.library_bend.repositories.BooksRepository;
import au.andrii.library_bend.util.LibraryCantSaveException;
import au.andrii.library_bend.util.LibraryDataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getBooks() {
        try {
//            return booksRepository.findAll(); // N+1 problem
            return booksRepository.getBooksWithUsers();
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't fetch books from DB");
        }
    }

    public Book findBookById(int id) {
        Optional<Book> foundedBook = Optional.empty();
        try {
            foundedBook = booksRepository.findById(id);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't get book from DB");
        }
        return foundedBook.orElseThrow(() -> new LibraryDataBaseException("Can't find book with ID " + id + " in DB"));

    }

    @Transactional
    public Book saveBook(Book book) {
        try {
            return booksRepository.save(book);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't save book in DB");
        }
    }

    @Transactional
    public void deleteBook(int id) {
        try {
            booksRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't delete book with ID " + id + " in DB");
        }
    }

    @Transactional
    public void test() {
        System.out.println("Test");
    }

    public List<Book> findBooksByTitleContaining(String searchingFragment) {
        try {
            return booksRepository.findBooksByTitleContaining(searchingFragment);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find book with title fragment " + searchingFragment + " in DB");
        }
    }

    public List<Book> findBooksByAuthorContaining(String searchingFragment) {
        try {
            return booksRepository.findBooksByAuthorContaining(searchingFragment);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find books with author fragment " + searchingFragment + " in DB");
        }
    }

    public List<Book> findBooksByIsAvailable(Boolean isAvailable) {
        try {
            return booksRepository.findBooksByIsAvailable(isAvailable);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find book(s) in DB");
        }
    }

    public void validateBook(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField() + "-" + error.getDefaultMessage() + ";");
            }
            throw new LibraryCantSaveException(errorMessage.toString());
        }
    }
}
