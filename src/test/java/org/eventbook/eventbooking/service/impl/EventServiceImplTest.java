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

import java.math.BigInteger;
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
        BigInteger id = new BigInteger("1");
        Event event = new Event();
        event.setId(id);
        Mockito.when(eventRepository.findById(id))
                .thenReturn(Optional.of(event));
        Event eventTest = eventService.getById(id);
        Mockito.verify(eventRepository).findById(id);
        Assertions.assertEquals(event, eventTest);
    }

   @Test
    void getCountById() {
       BigInteger id = new BigInteger("1");
       Event event = new Event();
       event.setId(id);
       Mockito.when(eventRepository.findCountById(id))
               .thenReturn(BigInteger.valueOf(700));
       BigInteger countTest = eventService.getCountById(id);
       Mockito.verify(eventRepository).findCountById(id);
       Assertions.assertEquals(BigInteger.valueOf(700), countTest);
    }

    @Test
    void create() {
        BigInteger eventId = new BigInteger("1");
        Event event = new Event();
        Mockito.doAnswer(invocation -> {
                    Event savedEvent = invocation.getArgument(0);
                    savedEvent.setId(eventId);
                    return savedEvent;
                })
                .when(eventRepository).save(event);
        eventService.create(event);
        Mockito.verify(eventRepository).save(event);
    }


/*    @Test
    void getAllEventByNameAndDurationAndCategory() throws ParseException {
        //BigInteger eventId = new BigInteger("1");
        String name = "You’ve Got What It Takes, But It Will Take Everything You’ve Got";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startedCreate = formatter.parse("2024-01-29");
        Date endedCreate = formatter.parse("2024-01-30");
        Event event = new Event();
        Mockito.when(eventRepository.findAllEventByNameAndDurationAndCategory());

    }*/

    @Test
    void uploadCountByEventId() {
        BigInteger eventId = new BigInteger("1");
        BigInteger count = BigInteger.valueOf(50);
        Event event = new Event();
        Mockito.when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));
        Event testEvent = eventService.uploadCountByEventId(eventId,count);
        Mockito.verify(eventRepository).uploadCountByEventId(eventId,count);
        Assertions.assertEquals(event, testEvent);
        Assertions.assertEquals(event.getAvailableAttendeesCount(), testEvent.getAvailableAttendeesCount());
    }
}
