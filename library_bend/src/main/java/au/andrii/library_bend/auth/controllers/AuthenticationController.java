package au.andrii.library_bend.auth.controllers;

import au.andrii.library_bend.auth.services.AuthenticationService;
import au.andrii.library_bend.dto.UserAuthDTO;
import au.andrii.library_bend.dto.UserDTO;
import au.andrii.library_bend.dto.UserRegistrationDTO;
import au.andrii.library_bend.models.User;
import au.andrii.library_bend.services.UserService;
import au.andrii.library_bend.util.LibraryCantSaveException;
import au.andrii.library_bend.util.LibraryDataBaseException;
import au.andrii.library_bend.util.ResponseLibrary;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(ModelMapper modelMapper, UserService userService, AuthenticationService authenticationService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> registration(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO,
                                                               BindingResult bindingResult) {
        userService.validateUser(bindingResult);
        User user = convertUserRegistrationDTOToUser(userRegistrationDTO);
        userService.saveUser(user);
        String token = authenticationService.register(user);
        return ResponseEntity.ok(new AuthenticationResponse(token, convertUserToUserDTO(user)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid UserAuthDTO userAuthDTO,
                                                               BindingResult bindingResult) {
        userService.validateUser(bindingResult);
//        String password = userAuthDTO.getPassword();
//        String encode = passwordEncoder.encode(password);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthDTO.getEmail(),
                        userAuthDTO.getPassword()));

        User user = userService.findUserByEmail(userAuthDTO.getEmail());
        String token = authenticationService.register(user);
        return ResponseEntity.ok(new AuthenticationResponse(token, convertUserToUserDTO(user)));
    }

    private User convertUserRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO) {
        return modelMapper.map(userRegistrationDTO, User.class);
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
