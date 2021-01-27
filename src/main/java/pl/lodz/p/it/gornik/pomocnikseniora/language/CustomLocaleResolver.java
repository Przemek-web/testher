package pl.lodz.p.it.gornik.pomocnikseniora.language;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class CustomLocaleResolver extends AcceptHeaderLocaleResolver{

    List<Locale> LOCALES = Arrays.asList(
            new Locale("en"),
            new Locale("pl"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }
//
    @Bean
    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
//        rs.setBasename("messages");
//        rs.setDefaultEncoding("UTF-8");
//        rs.setUseCodeAsDefaultMessage(true);
//        return rs;
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");  // name of the resource bundle
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
