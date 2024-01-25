package org.eventbook.eventbooking.web.security.expression;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.service.UserService;
import org.eventbook.eventbooking.web.security.JwtEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUser(final BigInteger id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        BigInteger userId = user.getId();

        return userId.equals(id);
    }

    public boolean canAccessEvent(final BigInteger taskId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        BigInteger id = user.getId();

        return userService.isEventOwner(id, taskId);
    }

}
