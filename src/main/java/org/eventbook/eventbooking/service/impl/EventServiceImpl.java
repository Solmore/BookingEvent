package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.event.Category;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;


    @Override
    @Transactional(readOnly = true)
    public Event getById(final BigInteger id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found")
                );
    }

    @Override
    @Transactional(readOnly = true)
    public BigInteger getCountById(final BigInteger id) {
        return eventRepository.findCountById(id);
    }

    @Override
    @Transactional
    public void create(final Event event) {
        eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEventByNameAndDuration(
            final String name,
            final Date startDate,
            final Date endDate) {
        if (name.isEmpty()) {
            if (startDate == null) {
                if (endDate == null) {
                    return eventRepository.findAll();
                }
                Date now = Date.from(LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                return eventRepository.findAllEventByDuration(
                        now,
                        endDate);
            }
            if (endDate == null) {
                return eventRepository.findAllEventByStatedDate(
                        startDate);
            }
            return eventRepository.findAllEventByDuration(
                    startDate,
                    endDate);
        }
        return eventRepository
                .findAllEventByNameAndDuration(
                        name,
                        startDate,
                        endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEventByNameAndDurationAndCategory(
            final String name,
            final Date startDate,
            final Date endDate,
            final Category category) {
        if (name.isEmpty()) {
            if (startDate == null) {
                if (endDate == null) {
                    return eventRepository.findAllEventByCategory(category);
                }
                Date now = Date.from(LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                return eventRepository.findAllEventByDurationAndCategory(
                        now,
                        endDate,
                        category);
            }
            if (endDate == null) {
                return
                        eventRepository
                        .findAllEventByStatedDateAndCategory(
                                startDate,
                                category);
            }
            return eventRepository
                    .findAllEventByDurationAndCategory(
                            startDate,
                            endDate,
                            category);
        }
        return eventRepository
                .findAllEventByNameAndDurationAndCategory(name,
                                                          startDate,
                                                          endDate,
                                                          category);
    }

    @Override
    @Transactional
    public Event uploadCountByEventId(final BigInteger eventId,
                                     final BigInteger availableCount) {
           eventRepository.uploadCountByEventId(eventId, availableCount);
           return getById(eventId);

    }
}
