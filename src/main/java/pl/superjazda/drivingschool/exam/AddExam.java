package pl.superjazda.drivingschool.exam;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AddExam {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date;

    public AddExam() {
    }

    public AddExam(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
