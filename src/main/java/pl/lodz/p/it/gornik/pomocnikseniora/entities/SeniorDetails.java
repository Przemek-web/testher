package pl.lodz.p.it.gornik.pomocnikseniora.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "senior_details")
public class SeniorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Version
    @NotNull
    @JsonIgnore
    @Column(name = "version")
    private Long version;

    @NotNull
    @Column(name = "level_name")
    private String levelName;

    @NotNull
    @Column(name = "points")
    private Integer points;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "lob")
    private byte[] lob;



    @Column(name = "accept_certificate")
    private Boolean acceptCertificate;


}
