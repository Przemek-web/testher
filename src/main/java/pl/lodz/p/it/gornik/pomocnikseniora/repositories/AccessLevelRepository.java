package pl.lodz.p.it.gornik.pomocnikseniora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.gornik.pomocnikseniora.enums.AccessLevelType;
import pl.lodz.p.it.gornik.pomocnikseniora.entities.AccessLevel;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AccessLevelRepository extends JpaRepository<AccessLevel, Long> {
    AccessLevel findByName(AccessLevelType name);
}
