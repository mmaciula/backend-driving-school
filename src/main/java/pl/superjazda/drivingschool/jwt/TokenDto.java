package pl.superjazda.drivingschool.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class TokenDto {
    private String token;
    private String header = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private List<String> roles;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    public TokenDto(String accessToken, Long id, String username, String email, String name, String surname, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }
}
