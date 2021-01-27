package pl.lodz.p.it.gornik.pomocnikseniora.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "volunteer_details")
public class VolunteerDetails {
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
    @Column(name = "cooking")
    private Boolean cooking;

    @NotNull
    @Column(name = "cleanup")
    private Boolean cleanup;

    @NotNull
    @Column(name = "transport")
    private Boolean transport;

    @NotNull
    @Column(name = "shopping")
    private Boolean shopping;

    @NotNull
    @Column(name = "rehabilitation")
    private Boolean rehabilitation;

    @NotNull
    @Column(name = "nursing")
    private Boolean nursing;

    @NotNull
    @Column(name = "monday")
    private Boolean monday;

    @NotNull
    @Column(name = "tuesday")
    private Boolean tuesday;

    @NotNull
    @Column(name = "wednesday")
    private Boolean wednesday;

    @NotNull
    @Column(name = "thursday")
    private Boolean thursday;

    @NotNull
    @Column(name = "friday")
    private Boolean friday;

    @NotNull
    @Column(name = "saturday")
    private Boolean saturday;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


}
