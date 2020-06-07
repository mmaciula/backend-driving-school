package pl.superjazda.drivingschool.exception;

import pl.superjazda.drivingschool.course.Course;

public class CourseHavePracticalException extends RuntimeException {
    public CourseHavePracticalException() { }
    public CourseHavePracticalException(String message) {
        super(message);
    }
    public CourseHavePracticalException(String message, Throwable cause) {
        super(message, cause);
    }
}
