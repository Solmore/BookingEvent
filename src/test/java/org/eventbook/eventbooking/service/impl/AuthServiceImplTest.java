package org.eventbook.eventbooking.service.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.repository.UserRepository;
import org.eventbook.eventbooking.web.dto.auth.Credentials;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;
import org.eventbook.eventbooking.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserServiceImpl userService;


    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthServiceImpl authService;


    @Test
    void auth() {
        Long userId = 1L;
        String email = "username@gmail.com";
        String password = "password";
        String Token = "Token";
        Credentials request = new Credentials();
        request.setEmail(email);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        Mockito.when(userService.getByEmail(email))
                .thenReturn(user);
        Mockito.when(tokenProvider.createToken(userId, email))
                .thenReturn(Token);
        CredentialsResponse response = authService.auth(request);
        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword())
                );
        Assertions.assertNotNull(response.getToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        String email = "username@gmail.com";
        String password = "password";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(password);
        Mockito.when(userService.getByEmail(email))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> authService.auth(credentials));
    }

    @Test
    void refresh(){
        String refreshToken = "refreshToken";
        String Token = "Token";
        String newRefreshToken = "newRefreshToken";
        CredentialsResponse credentialsResponse = new CredentialsResponse();
        credentialsResponse.setToken(Token);
        credentialsResponse.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshToken(refreshToken)).thenReturn(credentialsResponse);
        CredentialsResponse testCredentialResponse = authService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshToken(refreshToken);
        Assertions.assertEquals(testCredentialResponse,credentialsResponse);
    }
}
