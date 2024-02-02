package au.andrii.library_bend.dto;

import au.andrii.library_bend.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class BookDTO {

    private int id;

    @NotEmpty(message = "Title can't be empty")
    @Size(min = 4, max = 100, message = "Title must be between 4 and 100 characters")
    private String title;

    @NotEmpty(message = "Author can't be empty")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String author;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    private User user;

    public BookDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}


