package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.service.AuthService;
import org.eventbook.eventbooking.service.UserService;
import org.eventbook.eventbooking.web.dto.auth.Credentials;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;
import org.eventbook.eventbooking.web.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger applogger =
            LoggerFactory.getLogger("AppenderLog");

    @Override
    public CredentialsResponse auth(final Credentials authRequest) {
        applogger.info("Create tokens for authorization user");
        CredentialsResponse credentialsResponse = new CredentialsResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword())
        );
        User user = userService.getByEmail(authRequest.getEmail());
        credentialsResponse.setToken(jwtTokenProvider.createToken(
                user.getId(), user.getEmail()));
        credentialsResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                user.getId(), user.getEmail()
        ));
        return credentialsResponse;
    }

    @Override
    public CredentialsResponse refresh(final String refreshToken) {
        applogger.info("Refresh Access token for authorization user");
        return jwtTokenProvider.refreshToken(refreshToken);
    }
}
