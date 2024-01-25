package org.eventbook.eventbooking.web.security;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String email) {
        User user = userService.getByEmail(email);
        return JwtEntityFactory.create(user);
    }


}
