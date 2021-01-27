package pl.lodz.p.it.gornik.pomocnikseniora.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {

    private String login;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
