package pl.lodz.p.it.gornik.pomocnikseniora.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.VolunteerDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerReservationDto {

    private Long id;
    private VolunteerDetails volunteerDetails;
}
