package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.service.AuthService;
import org.eventbook.eventbooking.service.UserService;
import org.eventbook.eventbooking.web.dto.auth.Credentials;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;
import org.eventbook.eventbooking.web.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public CredentialsResponse auth(final Credentials authRequest) {
        CredentialsResponse credentialsResponse = new CredentialsResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword())
        );
        User user = userService.getByEmail(authRequest.getEmail());
        credentialsResponse.setToken(jwtTokenProvider.createToken(
                user.getId(), user.getEmail()));
        return credentialsResponse;
    }
}
