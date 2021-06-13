package pl.superjazda.drivingschool.web.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {}
    public CourseNotFoundException(String message) {
        super(message);
    }
    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
