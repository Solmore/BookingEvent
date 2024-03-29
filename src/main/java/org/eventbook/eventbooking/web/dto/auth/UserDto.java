package org.eventbook.eventbooking.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.dto.validation.OnUpdate;
import org.hibernate.validator.constraints.Length;

//import java.math.BigInteger;

@Getter
@Setter
@Schema(name = "User")
public class UserDto {

    @Schema(description = "User name", example = "Artem")
    @NotNull(message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 100,
            message = "Name length must be smaller than 100 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "User email", example = "artem@gmail.com")
    @NotNull(message = "Email must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Username length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @Schema(description = "User password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    private String password;

}
