package org.eventbook.eventbooking.service.impl;

import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.repository.EventRepository;
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

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventServiceImpl eventService;

    @Test
    void getById() {
        Long id = 1L;
        Event event = new Event();
        event.setId(id);
        Mockito.when(eventRepository.findById(id))
                .thenReturn(Optional.of(event));
        Event eventTest = eventService.getById(id);
        Mockito.verify(eventRepository).findById(id);
        Assertions.assertEquals(event, eventTest);
    }



    @Test
    void create() {
        Long eventId = 1L;
        Event event = new Event();
        Mockito.doAnswer(invocation -> {
                    Event savedEvent = invocation.getArgument(0);
                    savedEvent.setId(eventId);
                    return savedEvent;
                })
                .when(eventRepository).saveAndFlush(event);
        eventService.create(event);
        Mockito.verify(eventRepository).saveAndFlush(event);
    }



}
