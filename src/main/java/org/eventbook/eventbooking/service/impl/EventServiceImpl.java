package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public Event getById(final Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found")
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllSoonEvents(final Period period) {
        LocalDate now = LocalDate.now();
        return eventRepository.findAllSoonEvents(now, now.plus(period));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllByUserId(final Long id) {
        return Optional.ofNullable(eventRepository.findAllByUserId(id))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));
    }

    @Override
    @Transactional
    public void create(final Event event) {
        eventRepository.saveAndFlush(event);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEventByNameAndDurationAndCategory(
            final String name,
            final LocalDate startDate,
            final LocalDate endDate,
            final String category) {
        if (name.isEmpty()) {
            if (startDate == null) {
                if (endDate == null) {
                    return Optional.ofNullable(eventRepository
                            .findAllEventByCategory(category)).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Event not found"));
                }
                LocalDate now = LocalDate.from(LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                return Optional.ofNullable(eventRepository
                        .findAllEventByDurationAndCategory(
                        now,
                        endDate,
                        category)).orElseThrow(
                        () -> new ResourceNotFoundException("Event not found"));
            }
            if (endDate == null) {
                return Optional.ofNullable(eventRepository
                        .findAllEventByStartedDateAndCategory(
                                startDate,
                                category))
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Event not found"));
            }
            return Optional.ofNullable(eventRepository
                    .findAllEventByDurationAndCategory(
                            startDate,
                            endDate,
                            category))
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Event not found"));
        }
        String nameSearch = '%' + name;
        nameSearch += '%';
        if (startDate == null) {
            if (endDate == null) {
                return Optional.ofNullable(eventRepository.
                                findAllEventByNameAndCategory(nameSearch,
                                                              category))
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Event not found"));
            }
            LocalDate now = LocalDate.from(LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
            return Optional.ofNullable(eventRepository
                            .findAllEventByNameAndDurationAndCategory(
                                                           nameSearch,
                                                           now,
                                                           endDate,
                                                           category))
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Event not found"));
        }
        if (endDate == null) {
            return Optional.ofNullable(eventRepository
                            .findAllEventByNameAndStartedDateAndCategory(
                    nameSearch,
                    startDate,
                    category))
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Event not found"));
        }
        return Optional.ofNullable(eventRepository
                .findAllEventByNameAndDurationAndCategory(nameSearch,
                                                          startDate,
                                                          endDate,
                                                          category))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found"));
    }

}
