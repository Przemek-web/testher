package pl.lodz.p.it.gornik.pomocnikseniora.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import pl.lodz.p.it.gornik.pomocnikseniora.language.Translator;

public class InvalidLoginResponse {

    @Autowired
    ResourceBundleMessageSource messageSource;
    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = Translator.toLocale("label.userName");
        this.password = Translator.toLocale("label.password");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
