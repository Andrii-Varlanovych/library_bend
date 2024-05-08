package au.andrii.library_bend.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String register(UserDetails userDetails) {
        return jwtService.generateToken(userDetails);
    }
}
