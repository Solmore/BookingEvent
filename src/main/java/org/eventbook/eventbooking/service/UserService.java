package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.domain.user.User;

public interface UserService {

    User getByEmail(String email);

    void create(User user);

    boolean isEventOwner(Long userid, Long eventId);

}
