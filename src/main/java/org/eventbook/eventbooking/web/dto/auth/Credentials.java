package org.eventbook.eventbooking.web.dto.auth;

import lombok.Data;

@Data
public class Credentials {

    private String email;

    private String password;
}
