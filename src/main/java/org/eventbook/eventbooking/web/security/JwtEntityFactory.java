package org.eventbook.eventbooking.web.security;

import org.eventbook.eventbooking.domain.user.User;


public final class JwtEntityFactory {

    public static JwtEntity create(final User user) {
        return new JwtEntity(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword()
        );
    }

}
