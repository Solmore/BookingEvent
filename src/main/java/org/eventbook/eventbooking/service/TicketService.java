package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.domain.event.Event;

import java.math.BigInteger;


public interface TicketService {

    Event getById(Long id);

    Long getUserIdByEventId(Long id);

    void uploadCountByUserIdAndEventId(Long userId,
                                       Long eventId,
                                       BigInteger count);

    void createTicketByUserIdAndEventId(Long eventId,
                                        BigInteger count);

    void deleteTicketByUserIdAndEventId(Long userId,
                                        Long eventId);

    BigInteger getCountById(Long id);

    BigInteger getCountByUserIdAndEventId(Long userId,
                                          Long eventId);


    Event uploadCountByEventId(Long eventId,
                               BigInteger availableCount);
}
