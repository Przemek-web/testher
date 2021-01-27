package pl.lodz.p.it.gornik.pomocnikseniora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import pl.lodz.p.it.gornik.pomocnikseniora.language.CustomLocaleResolver;

@SpringBootApplication
@EnableScheduling
public class PomocniksenioraApplication {


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(PomocniksenioraApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

//    @Bean
//    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
//        return new ShallowEtagHeaderFilter();
//    }

//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//        source.setBasenames("i18n/messages");  // name of the resource bundle
//        source.setUseCodeAsDefaultMessage(true);
//        return source;
//    }

//@Bean
//public LocaleChangeInterceptor localeChangeInterceptor() {
//    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//    lci.setParamName("lang");
//    return lci;
//}


}
