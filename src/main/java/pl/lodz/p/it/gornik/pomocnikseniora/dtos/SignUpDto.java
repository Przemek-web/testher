package pl.lodz.p.it.gornik.pomocnikseniora.dtos;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Value
public class SignUpDto {


    @NotBlank(message = "Login is required")
    String login;

    @Email(message = "Username needs to be an email")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Password field is required")
    String password;

    @NotBlank(message = "Password confirmation is required")
    String confirmPassword;

    String firstName;

    String lastName;

    String phoneNumber;

    Set<String> role = new HashSet<>();


}
