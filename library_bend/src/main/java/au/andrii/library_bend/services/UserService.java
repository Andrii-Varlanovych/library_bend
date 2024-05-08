package au.andrii.library_bend.services;

import au.andrii.library_bend.models.Book;
import au.andrii.library_bend.models.Role;
import au.andrii.library_bend.models.User;
import au.andrii.library_bend.repositories.UserRepository;
import au.andrii.library_bend.util.LibraryCantSaveException;
import au.andrii.library_bend.util.LibraryDataBaseException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't fetch users from DB");
        }
    }

    public User findUserById(int id) {
        Optional<User> foundedUser = Optional.empty();
        try {
            foundedUser = userRepository.findById(id);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't get user from DB");
        }
        return foundedUser.orElseThrow(() -> new LibraryDataBaseException("Can't find user with ID " + id + " in DB"));
    }

    public User findUserByEmail(String email) {
        try {
            return userRepository.findUserByEmail(email).get();
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find user with email " + email + " in DB");
        }
    }

    public List<Book> getUserBooks(int id) {
        try {
            return userRepository.findBookListById(id);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't get user books from DB");
        }
    }

    public List<User> findUsersByNameContaining(String searchingFragment) {
        try {
            return userRepository.findUsersByNameContaining(searchingFragment);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find users with name fragment " + searchingFragment + " in DB");
        }
    }

    public List<User> findUsersBySurnameContaining(String searchingFragment) {
        try {
            return userRepository.findUsersBySurnameContaining(searchingFragment);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find users with surname fragment " + searchingFragment + " in DB");
        }
    }

    public List<User> findUsersByEmailContaining(String searchingFragment) {
        try {
            return userRepository.findUsersByEmailContaining(searchingFragment);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't find users with email fragment " + searchingFragment + " in DB");
        }
    }

    @Transactional
    public User saveUser(User user) {
        if(userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new LibraryCantSaveException("User with email " + user.getEmail() + " is already exists in DB");
        }

        user.setRole(Role.ROLE_USER.name());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userRepository.save(user);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't save user in DB");
        }
    }

    @Transactional
    public void deleteUser(int id) {
        try {
            userRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new LibraryDataBaseException("Can't delete user with ID " + id + " in DB");
        }
    }

    public void validateUser(BindingResult bindingResult) {
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
