package au.andrii.library_bend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserAuthDTO {
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "This is not an email")
    private String email;

    @NotEmpty(message = "Password can't be empty")
    @Size(min = 5, max = 20, message = "Password must be between 3 and 20 characters")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAuthDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
