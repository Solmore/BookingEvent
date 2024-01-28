package org.eventbook.eventbooking.domain.event;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.eventbook.eventbooking.domain.user.User;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;
    private BigInteger availableAttendeesCount;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @ManyToMany(mappedBy = "events")
    private Set<User> users;

}
