package pl.lodz.p.it.gornik.pomocnikseniora.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.enums.AccessLevelType;
import pl.lodz.p.it.gornik.pomocnikseniora.enums.ServiceType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "reservation_service")
public class ReservationService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Version
    @NotNull
    @JsonIgnore
    @Column(name = "version")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ServiceType name;

    @JoinColumn(name = "reservation_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private Reservation reservation;


    public ReservationService(Reservation reservation, ServiceType serviceType) {
        this.name = serviceType;
        this.reservation = reservation;

    }

    public ReservationService() {
    }
}
