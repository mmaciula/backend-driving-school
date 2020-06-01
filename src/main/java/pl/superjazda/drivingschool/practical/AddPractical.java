package pl.superjazda.drivingschool.practical;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AddPractical {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date;

    public AddPractical(Date date) {
        this.date = date;
    }

    public AddPractical() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
