package pl.superjazda.drivingschool.exception;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException() { }
    public CourseAlreadyExistsException(String message) {
        super(message);
    }
    public CourseAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
