package pl.lodz.p.it.gornik.pomocnikseniora.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccountDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.Image;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.SeniorDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.VolunteerDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterVolunteersDto {

    private Long id;
    private String email;
    private Image image;
    private AccountDetails accountDetails;
    private VolunteerDetails volunteerDetails;

}
