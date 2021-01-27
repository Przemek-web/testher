package pl.lodz.p.it.gornik.pomocnikseniora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccountDetails;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.User;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByLogin(String login);

    Optional<User> findById(Long id);

    Optional<User> findByEmail (String email);

    Optional<User> findByActivationCode(String activationCode);

    Optional<User> findByPassword(String password);

    Optional<User> findByResetPasswordCode(String resetPasswordCode);


    User getById(Long id); //findbyId 2:50

}
