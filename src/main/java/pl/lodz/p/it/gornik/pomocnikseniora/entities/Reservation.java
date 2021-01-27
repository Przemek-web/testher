package pl.lodz.p.it.gornik.pomocnikseniora.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Version
    @NotNull
    @JsonIgnore
    @Column(name = "version")
    private Long version;

    @JoinColumn(name = "volunteer_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private User volunteer;

    @JoinColumn(name = "senior_id", referencedColumnName = "id", updatable = false, nullable = false)
    @ManyToOne(optional = false)
    @NotNull
    private User senior;

    @NotNull
    @Column(name = "reservation_date")
    private String reservationDate;

    @NotNull
    @Column(name = "total_points")
    private Integer totalPoints;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
    private Collection<ReservationService> services = new ArrayList<>();


}
