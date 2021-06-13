package pl.superjazda.drivingschool.web.exception;

public class ExamNotFoundException extends RuntimeException {
    public ExamNotFoundException() { }
    public ExamNotFoundException(String message) {
        super(message);
    }
    public ExamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
