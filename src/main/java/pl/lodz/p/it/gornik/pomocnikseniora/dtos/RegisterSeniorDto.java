package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterSeniorDto {

    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String levelName;
}
