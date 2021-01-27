package pl.lodz.p.it.gornik.pomocnikseniora.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.User;
import pl.lodz.p.it.gornik.pomocnikseniora.language.Translator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {


    private JavaMailSender javaMailSender;

    @Value("http://localhost:3000")
    private String baseUrl;

    @Value("pomocnikseniora2021@gmail.com")
    private String email;


    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //    public void sendEmail(String to) {

//
//        try {
//            System.out.println("To: " + to);
//            SimpleMailMessage msg = new SimpleMailMessage();
//            msg.setTo(to);
//            msg.setSubject("Witaj w pomocnik seniora");
//            msg.setText("Zapewniamy kompleksową pomoc. \n " +
//                    "Zapraszamy!");
//
//            javaMailSender.send(msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void sendMessage(String subject, String text, String to) {
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            //helper.setFrom(email);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text);
//            javaMailSender.send(message);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }

//    public void sendAccountActivationEmail(User user) {
//        final String subject = "Aktywacja konta";
//        final String url = baseUrl + "/activation/" + user.getActivationCode();
//                String text = "To confirm your account, please click here : "
//                +"http://localhost:3000/activation?token="+ user.getActivationCode();
//        final String to = user.getEmail();
//
//
//
//        sendMessage(subject, text, to);
//    }

    public void sendPasswordResetEmail(User user)
            throws MessagingException, UnsupportedEncodingException{


        String toAddress = user.getEmail();
        String fromAddress = "pomocnikseniora2021@gmail.com";
        String senderName = "Pomocnik seniora";
        String subject = "Please reset your password";
        String content = Translator.toLocale("signup.greeting") + " [[name]],<br>"
                + "Please click the link below to reset password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">LINK</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getLogin());
        String verifyURL = "http://localhost:3000" + "/resetPassword/" + user.getResetPasswordCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        javaMailSender.send(message);
    }


    public void sendVerificationEmail(User user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "pomocnikseniora2021@gmail.com";
        String senderName = "Pomocnik seniora";
        String subject = "Please verify your registration";
        String content = Translator.toLocale("signup.greeting") + " [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">LINK</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getLogin());
        String verifyURL = "http://localhost:3000" + "/activation/" + user.getActivationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        javaMailSender.send(message);

    }










}
