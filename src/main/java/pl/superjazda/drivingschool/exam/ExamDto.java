package pl.superjazda.drivingschool.exam;

import java.util.Date;

public class ExamDto {
    private Long id;
    private Date examDate;
    private String studentUsername;
    private String studentName;
    private String instructorUsername;
    private String instructorName;
    private Long courseId;
    private boolean occupied;
    private boolean passed;

    public ExamDto() {
    }

    public ExamDto(Long id, Date examDate, String studentUsername, String studentName, String instructorUsername, String instructorName, Long courseId,
                   boolean occupied, boolean passed) {
        this.id = id;
        this.examDate = examDate;
        this.studentUsername = studentUsername;
        this.studentName = studentName;
        this.instructorUsername = instructorUsername;
        this.instructorName = instructorName;
        this.courseId = courseId;
        this.occupied = occupied;
        this.passed = passed;
    }

    public ExamDto(Exam exam) {
        this.id = exam.getId();
        this.examDate = exam.getExamDate();
        if (exam.getStudent() == null) {
            this.studentUsername = "";
            this.studentName = "";
        } else {
            this.studentUsername = exam.getStudent().getUsername();
            this.studentName = exam.getStudent().getName() + " " + exam.getStudent().getSurname();
        }
        if (exam.getInstructor() == null) {
            this.instructorName = "";
            this.instructorUsername = "";
        } else {
            this.instructorUsername = exam.getInstructor().getUsername();
            this.instructorName = exam.getInstructor().getName() + " " + exam.getInstructor().getSurname();
        }
        this.courseId = exam.getCourse().getId();
        this.occupied = exam.getOccupied();
        this.passed = exam.getPassed();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getInstructorUsername() {
        return instructorUsername;
    }

    public void setInstructorUsername(String instructorUsername) {
        this.instructorUsername = instructorUsername;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
