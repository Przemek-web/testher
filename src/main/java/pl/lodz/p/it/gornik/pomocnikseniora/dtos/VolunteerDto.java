package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerDto {

    private Long id;
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean cooking;
    private Boolean cleanup;
    private Boolean transport;
    private Boolean shopping;
    private Boolean rehabilitation;
    private Boolean nursing;

    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
}
