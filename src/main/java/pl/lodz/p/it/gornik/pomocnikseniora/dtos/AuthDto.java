package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AuthDto {
    @NotBlank(message = "Username cannot be blank")
    String login;

    @NotBlank(message = "Password cannot be blank")
    String password;
}
