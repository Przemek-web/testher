package pl.lodz.p.it.gornik.pomocnikseniora.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.ReservationService;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.User;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.UserAccessLevel;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllSeniorReservationsDto {

    private Integer totalPoints;
    private String reservationDate;
    private SeniorVolunteerListDto senior;
    private SeniorVolunteerListDto volunteer;
    private List<ReservationService> services;
}
