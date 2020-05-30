package pl.superjazda.drivingschool.contactform;

public class ContactDto {
    private String name;
    private String email;
    private String content;

    public ContactDto() {
    }

    public ContactDto(String name, String email, String content) {
        this.name = name;
        this.email = email;
        this.content = content;
    }

    public ContactDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
