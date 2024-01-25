package org.eventbook.eventbooking.domain.event;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String name;
    private Date date;
    private BigInteger availableAttendeesCount;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Category category;

}
