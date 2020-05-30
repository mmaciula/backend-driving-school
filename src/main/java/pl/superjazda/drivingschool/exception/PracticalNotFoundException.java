package pl.superjazda.drivingschool.exception;

public class PracticalNotFoundException extends RuntimeException {
    public PracticalNotFoundException() { }
    public PracticalNotFoundException(String message) {
        super(message);
    }
    public PracticalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
