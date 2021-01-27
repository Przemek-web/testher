package pl.lodz.p.it.gornik.pomocnikseniora.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.SeniorDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.VolunteerDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.repositories.SeniorDetailsRepository;
import pl.lodz.p.it.gornik.pomocnikseniora.repositories.VolunteerDetailsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {

    @Autowired
    private SeniorDetailsRepository seniorDetailsRepository;

//    @Scheduled(cron = "0 0 1 * * MON")




    @Scheduled(fixedRate = 600000)
    public void loadPoints() {

       List<SeniorDetails> seniorDetails = seniorDetailsRepository.findAll().stream().collect(Collectors.toList());
       if (!seniorDetails.isEmpty()) {
           for(SeniorDetails detail: seniorDetails) {
               if(detail.getLevelName().equals("NONE")) {
                   detail.setPoints(detail.getPoints() + 20);
                   seniorDetailsRepository.save(detail);
               } else if (detail.getLevelName().equals("LIGHT")) {
                   detail.setPoints(detail.getPoints() + 30);
                   seniorDetailsRepository.save(detail);
               } else if (detail.getLevelName().equals("MODERATE")) {
                   detail.setPoints(detail.getPoints() + 40);
                   seniorDetailsRepository.save(detail);
               } else if (detail.getLevelName().equals("SIGNIFICANT")) {
                   detail.setPoints(detail.getPoints() + 50);
                   seniorDetailsRepository.save(detail);
               }
           }
       }

      seniorDetailsRepository.findAll().stream().forEach(n-> System.out.println("Senior o id: "+n.getId() + " z punktami "+ n.getPoints()));


    }

}
