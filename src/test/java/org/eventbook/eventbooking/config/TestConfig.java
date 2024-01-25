package org.eventbook.eventbooking.config;

import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.repository.UserRepository;
import org.eventbook.eventbooking.service.impl.AuthServiceImpl;
import org.eventbook.eventbooking.service.impl.EventServiceImpl;
import org.eventbook.eventbooking.service.impl.UserServiceImpl;
import org.eventbook.eventbooking.service.props.JwtProperties;
import org.eventbook.eventbooking.web.security.JwtTokenProvider;
import org.eventbook.eventbooking.web.security.JwtUserDetailsService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg=="
        );
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(
            final UserRepository userRepository
    ) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public JwtTokenProvider tokenProvider(
            final UserRepository userRepository
    ) {
        return new JwtTokenProvider(
                userDetailsService(userRepository),
                jwtProperties());
    }

    @Bean
    public Configuration configuration() {
        return Mockito.mock(Configuration.class);
    }

    @Bean
    @Primary
    public AuthServiceImpl authService(
            final UserRepository userRepository,
            final AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(
                authenticationManager,
                userService(userRepository),
                tokenProvider(userRepository)
        );
    }

    @Bean
    @Primary
    public UserServiceImpl userService(
            final UserRepository userRepository
    ) {
        return new UserServiceImpl(
                userRepository,
                testPasswordEncoder()
        );
    }
    @Bean
    @Primary
    public EventServiceImpl eventService(
            final EventRepository eventRepository
    ) {
        return new EventServiceImpl(eventRepository);
    }


    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public EventRepository eventRepository() {
        return Mockito.mock(EventRepository.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

}
