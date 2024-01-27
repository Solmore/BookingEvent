package org.eventbook.eventbooking.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import org.eventbook.eventbooking.domain.event.Event;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @ManyToMany(cascade = {CascadeType.DETACH,
                           CascadeType.MERGE,
                           CascadeType.PERSIST,
                           CascadeType.REFRESH})
    @JoinTable(name = "users_events",
            joinColumns =  @JoinColumn(name = "user_id",
                                       referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id",
                                             referencedColumnName = "id"))
    private Set<Event> events;

}
