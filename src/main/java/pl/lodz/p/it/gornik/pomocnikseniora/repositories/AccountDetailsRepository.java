package pl.lodz.p.it.gornik.pomocnikseniora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccessLevel;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccountDetails;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {


    Optional<AccountDetails> findByLastName(String lastName);

    Optional<AccountDetails> findByUserId(Long id);
    Optional<AccountDetails> findByUserLogin(String login);

}
