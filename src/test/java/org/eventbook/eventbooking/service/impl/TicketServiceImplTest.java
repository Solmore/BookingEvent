package org.eventbook.eventbooking.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.MailType;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.io.IOException;
import java.math.BigInteger;
import java.util.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Autowired
    private TicketServiceImpl ticketService;

    @MockBean
    private MailServiceImpl mailService;



    @Test
    void getById() {
        Long id = 1L;
        Event event = new Event();
        event.setId(id);
        Mockito.when(eventRepository.findById(id))
                .thenReturn(Optional.of(event));
        Event testEvent = ticketService.getById(id);
        Mockito.verify(eventRepository).findById(id);
        Assertions.assertEquals(event,testEvent);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(eventRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> ticketService.getById(id));
        Mockito.verify(eventRepository).findById(id);
    }

    @Test
    void createTicketByUserIdAndEventId() {
        Long userId = 1L;
        String email = "user@example.com";
        Long eventId = 1L;
        String name = "Name";
        BigInteger count = BigInteger.valueOf(1);
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        user.setName(name);
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        Mockito.when(authentication1.getPrincipal()).thenReturn(email);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        ticketService.createTicketByUserIdAndEventId(eventId,count);
        Mockito.verify(userRepository).findByEmail(email);
        Mockito.verify(userRepository).createByUserIdAndEventId(userId,eventId,count);
        Mockito.doNothing().when(mailService).sendEmail(user,MailType.REGISTRATION,new Properties());
    }



    @Test
    void getUserIdByEventId() {
        Long eventId = 1L;
        Long userId = 1L;
        List<Long> usersId = new ArrayList<>();
        for (long i = 0L; i < 5L; i++) {
            usersId.add(i);
        }
        Mockito.when(userRepository.findUserIdByEventId(eventId))
                .thenReturn(usersId);
        Mockito.when(userRepository.isEventOwner(userId,eventId))
                .thenReturn(true);
        Long testUserId = ticketService.getUserIdByEventId(eventId);
        Mockito.verify(userRepository).findUserIdByEventId(eventId);
        Mockito.verify(userRepository).isEventOwner(userId, eventId);
        Assertions.assertEquals(userId,testUserId);
    }

    @Test
    void deleteTicketByUserIdAndEventId(){
        Long userId = 1L;
        Long eventId = 1L;
        ticketService.deleteTicketByUserIdAndEventId(userId,eventId);
        Mockito.verify(eventRepository).deleteTicketByUserIdAndEventId(userId,eventId);
    }

    @Test
    void getCountById() {
        Long id = 1L;
        BigInteger count = BigInteger.valueOf(1);
        Mockito.when(eventRepository.findCountById(id))
                .thenReturn(count);
        BigInteger testCount = ticketService.getCountById(id);
        Mockito.verify(eventRepository).findCountById(id);
        Assertions.assertEquals(count,testCount);
    }

    @Test
    void getCountByUserIdAndEventId(){
        Long userId = 1L;
        Long eventId = 1L;
        BigInteger count = BigInteger.valueOf(1);
        Mockito.when(userRepository.findCountByUserIdAndEventId(userId,eventId)).thenReturn(count);
        BigInteger testCount = ticketService.getCountByUserIdAndEventId(userId,eventId);
        Mockito.verify(userRepository).findCountByUserIdAndEventId(userId,eventId);
        Assertions.assertEquals(count,testCount);
    }

    @Test
    void uploadCountByUserIdAndEventId() {
        Long userId = 1L;
        Long eventId = 1L;
        BigInteger count = BigInteger.valueOf(1);
        ticketService.uploadCountByUserIdAndEventId(userId,eventId,count);
        Mockito.verify(userRepository).uploadCountByUserIdAndEventId(userId,eventId,count);
    }

    @Test
    void uploadCountByEventId() {
        Long eventId = 1L;
        BigInteger count = BigInteger.valueOf(1);
        Event event = new Event();
        event.setId(eventId);
        Mockito.when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));
        Event testEvent = ticketService.uploadCountByEventId(eventId,count);
        Mockito.verify(eventRepository).uploadCountByEventId(eventId,count);
        Assertions.assertEquals(event, testEvent);
    }
}
