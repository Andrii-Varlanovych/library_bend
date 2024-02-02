package au.andrii.library_bend.util;

public class LibraryCantSaveException extends RuntimeException {
    private String message;

    public LibraryCantSaveException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
