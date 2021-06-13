package pl.superjazda.drivingschool.web.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
