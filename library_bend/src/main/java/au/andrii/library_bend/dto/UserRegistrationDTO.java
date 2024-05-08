package au.andrii.library_bend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {

    @Column(name = "name")
    @NotEmpty(message = "Name can't be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname can't be empty")
    @Size(min = 3, max = 100, message = "Surname must be between 2 and 100 characters")
    private String surname;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "This is not an email")
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 5, max = 20, message = "Password must be between 3 and 20 characters")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRegistrationDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
