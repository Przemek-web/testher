package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    String login;
    String email;
    Boolean locked;
    Boolean activated;
    AccountDetails accountDetails;
    SeniorDetails seniorDetails;
    Image image;
    VolunteerDetails volunteerDetails;
    List<UserAccessLevel> roles;

}
