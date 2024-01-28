package org.eventbook.eventbooking.service.impl;

import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private TicketServiceImpl ticketService;

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
    void getUserIdByEventId() {
        Long eventId = 1L;
        List<Long> usersId = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            usersId.add((long) i);
        }
        Mockito.when(userRepository.findUserIdByEventId(Mockito.any()))
                .thenReturn(usersId);
        List<Long> testUsersId = userRepository.findUserIdByEventId(eventId);
        Mockito.verify(userRepository)
                .findUserIdByEventId(Mockito.any());
        Assertions.assertEquals(usersId, testUsersId);
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
