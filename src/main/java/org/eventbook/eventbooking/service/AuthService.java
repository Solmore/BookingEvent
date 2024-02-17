package org.eventbook.eventbooking.service;

import org.eventbook.eventbooking.web.dto.auth.Credentials;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;

public interface AuthService {

    CredentialsResponse auth(Credentials authRequest);

    CredentialsResponse refresh(String refreshToken);
}
