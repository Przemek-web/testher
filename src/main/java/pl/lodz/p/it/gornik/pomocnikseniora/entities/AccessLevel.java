package pl.lodz.p.it.gornik.pomocnikseniora.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.gornik.pomocnikseniora.enums.AccessLevelType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "access_level")
public class AccessLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Version
    @NotNull
    @JsonIgnore
    @Column(name = "version")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private AccessLevelType name;



    public AccessLevel() {

    }

    public AccessLevel(AccessLevelType name) {
        this.name = name;
    }


}
