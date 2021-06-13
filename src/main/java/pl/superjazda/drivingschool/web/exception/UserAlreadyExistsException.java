package pl.superjazda.drivingschool.web.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() { }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
