package org.eventbook.eventbooking.web.security.expression;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserRepository userRepository;

    public boolean canAccessUser(final Long id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found")
        );


        return user.getId().equals(id);
    }


}
