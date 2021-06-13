package pl.superjazda.drivingschool.api.contactform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    private String name;
    private String email;
    private String content;
}
