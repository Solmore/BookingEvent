package org.eventbook.eventbooking.service.impl;

import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EventServiceImpl eventService;

    @MockBean
    private AuthenticationManager authenticationManager;


    @Test
    void getUserById(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        User testUser = userService.getUserById(userId);
        Mockito.verify(userRepository).findById(userId);
        Assertions.assertEquals(user,testUser);
    }

    @Test
    void getUserByIdNotFound(){
        Long userId = 1L;
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(userId));
        Mockito.verify(userRepository).findById(userId);
    }

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
        Long userId = 1L;
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            events.add(new Event());
        }
        Mockito.when(eventRepository.findAllByUserId(userId))
                .thenReturn(events);
        List<Event> testEvent = eventService.getAllByUserId(userId);
        Mockito.verify(eventRepository).findAllByUserId(userId);
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
        Mockito.verify(userRepository).saveAndFlush(user);
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
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isEventOwner(userId, taskId))
                .thenReturn(true);
        boolean isOwner = userService.isEventOwner(userId, taskId);
        Mockito.verify(userRepository).isEventOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void getEventsRegistration(){
        Long eventId = 1L;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(new User());
        }
        Mockito.when(userRepository.findEventRegistration(eventId))
                .thenReturn(users);
        List<User> testUser = userService.getEventsRegistration(eventId);
        Mockito.verify(userRepository).findEventRegistration(eventId);
        Assertions.assertEquals(users, testUser);
    }

    @Test
    void getEventsRegistrationNotFound(){
        Long eventId = 1L;
        Mockito.when(userRepository.findEventRegistration(eventId)).thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getEventsRegistration(eventId));
        Mockito.verify(userRepository).findEventRegistration(eventId);
    }

    @Test
    void isEventOwner(){
        Long userId = 1L;
        Long eventId = 1L;
        Mockito.when(userRepository.isEventOwner(userId,eventId))
                .thenReturn(false);
        boolean isOwner = userService.isEventOwner(userId, eventId);
        Mockito.verify(userRepository).isEventOwner(userId, eventId);
        Assertions.assertFalse(isOwner);
    }


}
