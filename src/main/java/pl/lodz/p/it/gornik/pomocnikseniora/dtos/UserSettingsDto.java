package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccessLevel;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.UserAccessLevel;


import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDto {
    Long id;
    String login;
    Boolean locked;
    List<UserAccessLevel> roles;
}
