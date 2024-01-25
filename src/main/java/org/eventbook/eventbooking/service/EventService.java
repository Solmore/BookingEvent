package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.domain.event.Category;
import org.eventbook.eventbooking.domain.event.Event;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface EventService {

    Event getById(BigInteger id);

    BigInteger getCountById(BigInteger id);

    void create(Event event);

    List<Event> getAllEventByNameAndDuration(String name,
                                             Date startDate,
                                             Date endDate);


    List<Event> getAllEventByNameAndDurationAndCategory(String name,
                                                        Date startDate,
                                                        Date endDate,
                                                        Category category);

    Event uploadCountByEventId(BigInteger eventId,
                              BigInteger availableCount);
}
