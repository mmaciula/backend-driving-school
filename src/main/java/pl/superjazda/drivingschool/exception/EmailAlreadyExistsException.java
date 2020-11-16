package pl.superjazda.drivingschool.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() { }
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
