package pl.superjazda.drivingschool.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Register {
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

    public Register(String username, String email, String password, String name, String surname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
}
