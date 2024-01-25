package org.eventbook.eventbooking.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(hidden = true)
public class CredentialsResponse {

    private String token;

}
