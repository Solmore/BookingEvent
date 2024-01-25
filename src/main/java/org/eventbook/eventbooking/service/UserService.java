package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.user.User;

import java.math.BigInteger;
import java.util.List;

public interface UserService {

    User getByEmail(String email);

    void create(User user);

    List<Event> getAllByUserId(BigInteger id);

    boolean isEventOwner(BigInteger userid, BigInteger eventId);

}
