package pl.superjazda.drivingschool.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() { }
    public RoleNotFoundException(String message) {
        super(message);
    }
    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
