package pl.superjazda.drivingschool.api.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
