package pl.lodz.p.it.gornik.pomocnikseniora.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_access_level")
public class UserAccessLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Version
    @NotNull
    @JsonIgnore
    @Column(name = "version")
    private Long version;


    @JoinColumn(name = "access_level_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    private AccessLevel accessLevel;
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private User user;

    public UserAccessLevel() {
    }


    public UserAccessLevel(User user, AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        this.user = user;

    }
}
