package au.andrii.library_bend.controllers;

import au.andrii.library_bend.dto.BookDTO;
import au.andrii.library_bend.dto.UserDTO;
import au.andrii.library_bend.models.User;
import au.andrii.library_bend.services.UserService;
import au.andrii.library_bend.util.LibraryCantSaveException;
import au.andrii.library_bend.util.LibraryDataBaseException;
import au.andrii.library_bend.util.ResponseLibrary;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final BooksController booksController;

    public UsersController(UserService userService, ModelMapper modelMapper, BooksController booksController) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.booksController = booksController;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") int id) {
        return convertUserToUserDTO(userService.findUserById(id));
    }

    @GetMapping("/{id}/user-books")
    public List<BookDTO> getUserBooks(@PathVariable("id") int id) {
        return userService.getUserBooks(id).stream()
                .map(booksController::convertBookToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/name/{searchingFragment}")
    public List<UserDTO> findUsersByNameContaining(@PathVariable("searchingFragment") String searchingFragment) {
        return userService.findUsersByNameContaining(searchingFragment).stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/surname/{searchingFragment}")
    public List<UserDTO> findUsersBySurnameContaining(@PathVariable("searchingFragment") String searchingFragment) {
        return userService.findUsersBySurnameContaining(searchingFragment).stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/email/{searchingFragment}")
    public List<UserDTO> findUsersByEmailContaining(@PathVariable("searchingFragment") String searchingFragment) {
        return userService.findUsersByEmailContaining(searchingFragment).stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    @PostMapping()
    public UserDTO saveUser(@RequestBody @Valid UserDTO userDTO,
                            BindingResult bindingResult) {
        userService.validateUser(bindingResult);
        User savedUser = userService.saveUser(convertUserDTOToUser(userDTO));
        return convertUserToUserDTO(savedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable("id") int id, @RequestBody UserDTO userDTO,
                              BindingResult bindingResult) {
        userService.validateUser(bindingResult);
        User updatedUser = userService.saveUser(convertUserDTOToUser(userDTO));
        return convertUserToUserDTO(updatedUser);
    }

    private User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
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
