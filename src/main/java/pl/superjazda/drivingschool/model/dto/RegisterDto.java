package pl.superjazda.drivingschool.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class RegisterDto {
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;
    @Email
    @NotBlank
    @Size(max = 50)
    private String email;
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private Set<String> roles;

    public RegisterDto() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
