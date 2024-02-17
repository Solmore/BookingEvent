package org.eventbook.eventbooking.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(hidden = true)
public class CredentialsResponse {

    private String token;

    private String refreshToken;

}
