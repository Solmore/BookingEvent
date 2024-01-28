package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eventbook.eventbooking.domain.MailType;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.service.EventService;
import org.eventbook.eventbooking.service.MailService;
import org.eventbook.eventbooking.service.Reminder;
import org.eventbook.eventbooking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderImpl implements Reminder {

    private final EventService eventService;
    private final UserService userService;
    private final MailService mailService;
    private final Period period = Period.ofDays(1);
    private final Logger auditlogger =
            LoggerFactory.getLogger("AuditLog");


    @Scheduled(cron = "0 0 12 * * *")
    @Override
    public void remindForTask() {
        List<Event> events = eventService.getAllSoonEvents(period);
        events.forEach(event -> {
            List<User> users = userService.getEventsRegistration(event.getId());
            users.forEach(user -> {
                auditlogger.info("Remind about event to user = {} "
                                + "and ever = {}",
                        user.getId(),
                        event.getId());
                Properties properties = new Properties();
                properties.setProperty("event.name", event.getName());
                properties.setProperty("event.description",
                        event.getDescription());
                properties.setProperty("event.date",
                                event.getDate().toString());
                mailService.sendEmail(user, MailType.REMINDER, properties);
            }
            );
        });
    }
}
