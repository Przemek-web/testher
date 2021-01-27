package pl.lodz.p.it.gornik.pomocnikseniora.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Version
    @NotNull
    @JsonIgnore
    @Column(name = "version")
    private Long version;

//    @NotBlank(message = "username is required")
    @Column(name = "login")
    private String login;

    @Email(message = "Username needs to be an email")
//    @NotBlank(message = "username is required")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password field is required")
    @Column(name = "password")
    private String password;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "invalid_login_attempts")
    private Integer invalidLoginAttempts;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "reset_password_code")
    private String resetPasswordCode;



    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user", orphanRemoval = true)
    private Image image;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Collection<UserAccessLevel> roles = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private AccountDetails accountDetails;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private VolunteerDetails volunteerDetails;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private SeniorDetails seniorDetails;



    public User() {
    }



}

