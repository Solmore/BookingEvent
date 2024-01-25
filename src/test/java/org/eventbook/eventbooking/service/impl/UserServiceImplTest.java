package org.eventbook.eventbooking.service.impl;

import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserServiceImpl userService;


    @Test
    void getByEmail(){
        String email = "username@gmail.com";
        User user = new User();
        user.setEmail(email);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByEmail(email);
        Mockito.verify(userRepository).findByEmail(email);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistEmail(){
        String email = "username@gmail.com";
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail(email));
        Mockito.verify(userRepository).findByEmail(email);
    }

    @Test
    void getAllByUserId() {
        BigInteger userId = new BigInteger("1");
        User user = new User();
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            events.add(new Event());
        }
        Mockito.when(userRepository.findAllByUserId(userId))
                .thenReturn(events);
        List<Event> testEvent = userService.getAllByUserId(userId);
        Mockito.verify(userRepository).findAllByUserId(userId);
        Assertions.assertEquals(events, testEvent);
    }

    @Test
    void create() {
        String email = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        userService.create(user);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void createWithExistingEmail() {
        String email = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }


    @Test
    void isTaskOwner() {
        BigInteger userId = new BigInteger("1");
        BigInteger taskId = new BigInteger("1");
        Mockito.when(userRepository.isEventOwner(userId, taskId))
                .thenReturn(true);
        boolean isOwner = userService.isEventOwner(userId, taskId);
        Mockito.verify(userRepository).isEventOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }


}
