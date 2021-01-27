package pl.lodz.p.it.gornik.pomocnikseniora.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivationUserDto {

    private String login;
    private String activationCode;

}
