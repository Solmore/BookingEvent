package org.eventbook.eventbooking.service.impl;

import org.eventbook.eventbooking.config.TestConfig;
import org.eventbook.eventbooking.domain.event.Category;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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
    void getByIdNotFound() {
        Long id = 1L;
        Mockito.when(eventRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.getById(id));
        Mockito.verify(eventRepository).findById(id);
    }

    @Test
    void getAllSoonEvents(){
        Period period = Period.ofDays(1);
        LocalDate localDate = LocalDate.now();
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        Mockito.when(eventRepository.findAllSoonEvents(localDate,localDate.plus(period)))
                .thenReturn(events);
        List<Event> testEvents = eventService.getAllSoonEvents(period);
        Mockito.verify(eventRepository).findAllSoonEvents(localDate,localDate.plus(period));
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllByUserId(){
        Long userId = 1L;
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        Mockito.when(eventRepository.findAllByUserId(userId)).thenReturn(events);
        List<Event> testEvents = eventService.getAllByUserId(userId);
        Mockito.verify(eventRepository).findAllByUserId(userId);
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllByUserIdNotFound(){
        Long userId = 1L;
        Mockito.when(eventRepository.findAllByUserId(userId)).thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.getAllByUserId(userId));
        Mockito.verify(eventRepository).findAllByUserId(userId);
    }

    @Test
    void getAllEventByNameAndDurationAndCategory(){
        String name = "name";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate =  LocalDate.now().plusDays(7);
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        String nameSearch = '%' + name;
        nameSearch += '%';
        Mockito.when(eventRepository
                .findAllEventByNameAndDurationAndCategory(nameSearch,startDate,endDate,category))
                .thenReturn(events);
        List<Event> testEvents = eventService
                .getAllEventByNameAndDurationAndCategory(name,startDate,endDate,category);
        Mockito.verify(eventRepository)
                .findAllEventByNameAndDurationAndCategory(nameSearch,startDate,endDate,category);
        Assertions.assertEquals(events,testEvents);

    }

    @Test
    void getAllEventByNameAndDurationAndCategoryNotFound(){
        String name = "name";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate =  LocalDate.now().plusDays(7);
        String category = "Concert";
        String nameSearch = '%' + name;
        nameSearch += '%';
        Mockito.when(eventRepository
                        .findAllEventByNameAndDurationAndCategory(nameSearch,startDate,endDate,category))
                .thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.getAllEventByNameAndDurationAndCategory(name,startDate,endDate,category));
        Mockito.verify(eventRepository)
                .findAllEventByNameAndDurationAndCategory(nameSearch,startDate,endDate,category);
    }

    @Test
    void getAllEventByDurationAndCategory(){
        LocalDate startDate = LocalDate.now();
        LocalDate endDate =  LocalDate.now().plusDays(7);
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        Mockito.when(eventRepository
                        .findAllEventByDurationAndCategory(startDate,endDate,category))
                .thenReturn(events);
        List<Event> testEvents = eventService
                .getAllEventByNameAndDurationAndCategory("",startDate,endDate,category);
        Mockito.verify(eventRepository).findAllEventByDurationAndCategory(startDate,endDate,category);
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllEventByDurationAndCategoryNotFound(){
        LocalDate startDate = LocalDate.now();
        LocalDate endDate =  LocalDate.now().plusDays(7);
        String category = "Concert";
        Mockito.when(eventRepository
                        .findAllEventByDurationAndCategory(startDate,endDate,category))
                .thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.getAllEventByNameAndDurationAndCategory("",startDate,endDate,category));
        Mockito.verify(eventRepository).findAllEventByDurationAndCategory(startDate,endDate,category);
    }

    @Test
    void getAllEventByStartDateAndCategory(){
        LocalDate startDate = LocalDate.now();
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        Mockito.when(eventRepository
                        .findAllEventByStartedDateAndCategory(startDate,category))
                .thenReturn(events);
        List<Event> testEvents = eventService
                .getAllEventByNameAndDurationAndCategory("",startDate,null,category);
        Mockito.verify(eventRepository).findAllEventByStartedDateAndCategory(startDate,category);
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllEventByStartDateAndCategoryNotFound(){
        LocalDate startDate = LocalDate.now();
        String category = "Concert";
        List<Event> events = new ArrayList<>();

        Mockito.when(eventRepository
                        .findAllEventByStartedDateAndCategory(startDate,category))
                .thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService
                        .getAllEventByNameAndDurationAndCategory("",startDate,null,category));
        Mockito.verify(eventRepository).findAllEventByStartedDateAndCategory(startDate,category);
    }

    @Test
    void getAllEventByCategory(){
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        Mockito.when(eventRepository
                        .findAllEventByCategory(category))
                .thenReturn(events);
        List<Event> testEvents = eventService
                .getAllEventByNameAndDurationAndCategory("",null,null,category);
        Mockito.verify(eventRepository).findAllEventByCategory(category);
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllEventByCategoryNotFound(){
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        Mockito.when(eventRepository
                        .findAllEventByCategory(category))
                .thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService
                        .getAllEventByNameAndDurationAndCategory("",null,null,category));
        Mockito.verify(eventRepository).findAllEventByCategory(category);
    }

    @Test
    void getAllEventByNameAndStartedDateAndCategory(){
        String name = "name";
        LocalDate startDate = LocalDate.now();
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        String nameSearch = '%' + name;
        nameSearch += '%';
        Mockito.when(eventRepository
                        .findAllEventByNameAndStartedDateAndCategory(nameSearch,startDate,category))
                .thenReturn(events);
        List<Event> testEvents = eventService.getAllEventByNameAndDurationAndCategory(name,startDate,null,category);
        Mockito.verify(eventRepository).findAllEventByNameAndStartedDateAndCategory(nameSearch,startDate,category);
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllEventByNameAndStartedDateAndCategoryNotFound(){
        String name = "name";
        LocalDate startDate = LocalDate.now();
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        String nameSearch = '%' + name;
        nameSearch += '%';
        Mockito.when(eventRepository
                        .findAllEventByNameAndStartedDateAndCategory(nameSearch,startDate,category))
                .thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService
                        .getAllEventByNameAndDurationAndCategory(name,startDate,null,category));
        Mockito.verify(eventRepository).findAllEventByNameAndStartedDateAndCategory(nameSearch,startDate,category);
    }

    @Test
    void getAllEventByNameAndCategory(){
        String name = "name";
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        for (long i = 0L; i < 3L; i++) {
            events.add(new Event());
        }
        String nameSearch = '%' + name;
        nameSearch += '%';
        Mockito.when(eventRepository
                        .findAllEventByNameAndCategory(nameSearch,category))
                .thenReturn(events);
        List<Event> testEvents = eventService.getAllEventByNameAndDurationAndCategory(name,null,null,category);
        Mockito.verify(eventRepository).findAllEventByNameAndCategory(nameSearch,category);
        Assertions.assertEquals(events,testEvents);
    }

    @Test
    void getAllEventByNameAndCategoryNotFound(){
        String name = "name";
        String category = "Concert";
        List<Event> events = new ArrayList<>();
        String nameSearch = '%' + name;
        nameSearch += '%';
        Mockito.when(eventRepository
                        .findAllEventByNameAndCategory(nameSearch,category))
                .thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.getAllEventByNameAndDurationAndCategory(name,null,null,category));
        Mockito.verify(eventRepository).findAllEventByNameAndCategory(nameSearch,category);
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
