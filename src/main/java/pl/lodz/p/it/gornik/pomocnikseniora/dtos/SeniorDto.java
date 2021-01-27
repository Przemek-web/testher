package pl.lodz.p.it.gornik.pomocnikseniora.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccountDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.Image;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.SeniorDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeniorDto {

    private Long id;
    private String email;
    private Image image;
    private AccountDetails accountDetails;
    private SeniorDetails seniorDetails;
}
