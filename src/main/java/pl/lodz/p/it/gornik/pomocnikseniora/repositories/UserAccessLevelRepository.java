package pl.lodz.p.it.gornik.pomocnikseniora.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccessLevel;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.User;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.UserAccessLevel;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserAccessLevelRepository extends JpaRepository<UserAccessLevel, Long> {

    List<UserAccessLevel> findByAccessLevelIdEquals(Long id);

}
