package pl.lodz.p.it.gornik.pomocnikseniora.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.lodz.p.it.gornik.pomocnikseniora.dtos.SignUpDto;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.User;

@Component
public class RegisterValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) object;

        if (signUpDto.getPassword().length() < 6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }

        if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }
    }
}

