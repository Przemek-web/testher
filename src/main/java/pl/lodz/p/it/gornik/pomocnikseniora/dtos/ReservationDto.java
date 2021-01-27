package pl.lodz.p.it.gornik.pomocnikseniora.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Integer totalPoints;
    private String reservationDate;
    private Long seniorId;
    private Long volunteerId;

    private Boolean cookingService;
    private Boolean cleanupService;
    private Boolean transportService;
    private Boolean shoppingService;
    private Boolean rehabilitationService;
    private Boolean nursingService;
}
