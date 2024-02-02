package au.andrii.library_bend.controllers;

import au.andrii.library_bend.dto.BookDTO;
import au.andrii.library_bend.dto.UserDTO;
import au.andrii.library_bend.models.Book;
import au.andrii.library_bend.models.User;
import au.andrii.library_bend.services.BooksService;
import au.andrii.library_bend.util.LibraryCantSaveException;
import au.andrii.library_bend.util.LibraryDataBaseException;
import au.andrii.library_bend.util.ResponseLibrary;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;

    private final ModelMapper modelMapper;

    @Autowired
    public BooksController(BooksService booksService, ModelMapper modelMapper) {
        this.booksService = booksService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<BookDTO> getBooks() {
        booksService.test();
        List<BookDTO> collect = booksService.getBooks().stream()
                .map(this::convertBookToBookDTO)
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable("id") int id) {
        return convertBookToBookDTO(booksService.findBookById(id));
    }

    @PostMapping
    public BookDTO saveBook(@RequestBody @Valid BookDTO bookDTO,
                         BindingResult bindingResult) {
        booksService.validateBook(bindingResult);
        Book savedBook = booksService.saveBook(convertBookDTOToBook(bookDTO));
        return convertBookToBookDTO(savedBook);
    }

    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable("id") int id, @RequestBody @Valid BookDTO bookDTO,
                         BindingResult bindingResult) {
        booksService.validateBook(bindingResult);
        Book updatedBook = booksService.saveBook(convertBookDTOToBook(bookDTO));
        return convertBookToBookDTO(updatedBook);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") int id) {
        booksService.deleteBook(id);
    }

    @GetMapping("/title/{searchingFragment}")
    public List<BookDTO> findBookByTitleContaining(@PathVariable("searchingFragment") String searchingFragment) {
        return booksService.findBooksByTitleContaining(searchingFragment).stream()
                .map(this::convertBookToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/author/{searchingFragment}")
    public List<BookDTO> findBooksByAuthorContaining(@PathVariable("searchingFragment") String searchingFragment) {
        return booksService.findBooksByAuthorContaining(searchingFragment).stream()
                .map(this::convertBookToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/isAvailable/{isAvailable}")
    public List<BookDTO> findBooksByIsAvailable(@PathVariable("isAvailable") Boolean isAvailable) {
        return booksService.findBooksByIsAvailable(isAvailable).stream()
                .map(this::convertBookToBookDTO)
                .collect(Collectors.toList());
    }

    private Book convertBookDTOToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    public BookDTO convertBookToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseLibrary> handleDataBaseException(LibraryDataBaseException e) {
        ResponseLibrary response = new ResponseLibrary(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseLibrary> handleCantSaveException(LibraryCantSaveException e) {
        ResponseLibrary response = new ResponseLibrary(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
