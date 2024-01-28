package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.domain.MailType;
import org.eventbook.eventbooking.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType type, Properties params);

}
