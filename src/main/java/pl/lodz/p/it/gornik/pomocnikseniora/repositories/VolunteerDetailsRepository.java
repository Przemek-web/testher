package pl.lodz.p.it.gornik.pomocnikseniora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccountDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.SeniorDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.VolunteerDetails;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface VolunteerDetailsRepository extends JpaRepository<VolunteerDetails, Long> {

    Optional<VolunteerDetails> findByUserId(Long id);
}
