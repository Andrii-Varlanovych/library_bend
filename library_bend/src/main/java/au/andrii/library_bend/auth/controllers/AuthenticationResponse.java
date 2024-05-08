package au.andrii.library_bend.auth.controllers;

import au.andrii.library_bend.dto.UserDTO;

public class AuthenticationResponse {
    private String token;
    private UserDTO user;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
