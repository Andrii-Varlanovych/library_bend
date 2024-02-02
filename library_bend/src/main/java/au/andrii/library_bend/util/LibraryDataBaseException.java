package au.andrii.library_bend.util;

public class LibraryDataBaseException extends RuntimeException {
    private String message;

    public LibraryDataBaseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
