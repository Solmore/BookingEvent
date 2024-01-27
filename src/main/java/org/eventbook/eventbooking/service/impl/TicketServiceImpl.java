package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.repository.UserRepository;
import org.eventbook.eventbooking.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final EventRepository eventRepository;

    private  final UserRepository userRepository;

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
    public Long getUserIdByEventId(final Long eventId) {
        List<Long> usersId = userRepository.findUserIdByEventId(eventId);
        Long userId = 0L;
        for (Long id: usersId) {
            if (userRepository.isEventOwner(id, eventId)) {
                userId = id;
            }
        }
        return userId;
    }
    @Override
    @Transactional
    public void createTicketByUserIdAndEventId(final Long eventId,
                                               final BigInteger count) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Event not found")
        );
        userRepository.createByUserIdAndEventId(user.getId(), eventId, count);
    }

    public void deleteTicketByUserIdAndEventId(final Long userId,
                                        final Long eventId) {
        eventRepository.deleteTicketByUserIdAndEventId(userId,
                                                       eventId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigInteger getCountById(final Long id) {
        return eventRepository.findCountById(id);
    }

    @Override
    @Transactional
    public void uploadCountByUserIdAndEventId(final Long userId,
                                              final Long eventId,
                                              final BigInteger count) {
        userRepository.uploadCountByUserIdAndEventId(userId, eventId, count);
    }

    @Override
    @Transactional
    public Event uploadCountByEventId(final Long eventId,
                                      final BigInteger availableCount) {
        eventRepository.uploadCountByEventId(eventId, availableCount);
        return getById(eventId);
    }
}
