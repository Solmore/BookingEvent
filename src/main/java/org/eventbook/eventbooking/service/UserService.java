package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.domain.user.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    User getByEmail(String email);

    void create(User user);

    List<User> getEventsRegistration(Long eventId);

    boolean isEventOwner(Long userid, Long eventId);

}
