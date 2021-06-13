package pl.superjazda.drivingschool.web.exception;

public class CourseHavePracticalException extends RuntimeException {
    public CourseHavePracticalException() { }
    public CourseHavePracticalException(String message) {
        super(message);
    }
    public CourseHavePracticalException(String message, Throwable cause) {
        super(message, cause);
    }
}
