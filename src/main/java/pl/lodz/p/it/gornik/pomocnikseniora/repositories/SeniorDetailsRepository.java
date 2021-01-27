package pl.lodz.p.it.gornik.pomocnikseniora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccountDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.SeniorDetails;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface SeniorDetailsRepository extends JpaRepository<SeniorDetails, Long> {
    Optional<SeniorDetails> findByUserId(Long id);
}
