package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eventbook.eventbooking.domain.MailType;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.EventRepository;
import org.eventbook.eventbooking.repository.UserRepository;
import org.eventbook.eventbooking.service.MailService;
import org.eventbook.eventbooking.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final EventRepository eventRepository;
    private  final UserRepository userRepository;

    private final MailService mailService;

    private final Logger auditlogger =
            LoggerFactory.getLogger("AuditLog");

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
        auditlogger.info("Register reserving the event by user. EventId = {}",
                eventId.toString());
        mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
    }

    public void deleteTicketByUserIdAndEventId(final Long userId,
                                        final Long eventId) {
        eventRepository.deleteTicketByUserIdAndEventId(userId,
                                                       eventId);
        auditlogger.info("Cancel reserving the event by user. EventId = {}",
                eventId.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public BigInteger getCountById(final Long id) {
        return eventRepository.findCountById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigInteger getCountByUserIdAndEventId(final Long userId,
                                                 final Long eventId) {
        return userRepository.findCountByUserIdAndEventId(userId, eventId);
    }

    @Override
    @Transactional
    public void uploadCountByUserIdAndEventId(final Long userId,
                                              final Long eventId,
                                              final BigInteger count) {
        userRepository.uploadCountByUserIdAndEventId(userId, eventId, count);
        auditlogger.info("Register reserving on the table users_events. "
                + "EventID = {}", eventId.toString());
    }

    @Override
    @Transactional
    public Event uploadCountByEventId(final Long eventId,
                                      final BigInteger availableCount) {
        eventRepository.uploadCountByEventId(eventId, availableCount);
        auditlogger.info("Register reserving on the table events. EventID= {}",
                eventId.toString());
        return getById(eventId);
    }
}
