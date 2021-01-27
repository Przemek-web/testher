package pl.lodz.p.it.gornik.pomocnikseniora.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserSettingsRequest {
    private String login;
    private Boolean admin;
    private Boolean manager;
    private Boolean user;
    private Boolean locked;

}
