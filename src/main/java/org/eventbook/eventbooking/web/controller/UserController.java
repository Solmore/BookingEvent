package org.eventbook.eventbooking.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.service.AuthService;
import org.eventbook.eventbooking.service.EventService;
import org.eventbook.eventbooking.service.UserService;
import org.eventbook.eventbooking.web.dto.auth.Credentials;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;
import org.eventbook.eventbooking.web.dto.auth.UserDto;
import org.eventbook.eventbooking.web.dto.event.EventDto;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.mapper.EventMapper;
import org.eventbook.eventbooking.web.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Tag(name = "Users")
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    private final AuthService authService;
    private final UserService userService;
    private final EventService eventService;
    private final Logger applogger = LoggerFactory.getLogger("AppenderLog");


    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate a user.",
            description = "This endpoint allows users to authenticate"
                    + " and receive a Bearer token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation =
                                            CredentialsResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content) })
    public CredentialsResponse auth(
                             @Parameter(description = "The user's credentials.")
                             @Validated(OnCreate.class)
                             @RequestBody final Credentials authRequest) {
        applogger.info("Controller method to authenticate a user");
        return authService.auth(authRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate a user.",
            description = "This endpoint allows users to authenticate"
                    + " and receive a Bearer token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content) })
    public CredentialsResponse refresh(
            @Parameter(description = "The user's credentials.")
            @RequestBody final String refreshToken) {
        applogger.info("Controller method to refresh a user");
        return authService.refresh(refreshToken);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user.",
            description = "This endpoint allows"
                    + " customers to create a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Bad request",
                    content = @Content) })
    public void createUser(@Parameter(description = "The user to create.")
                       @Validated(OnCreate.class)
                       @RequestBody final UserDto userDto) {
        applogger.info("Controller method to create a user");
        User user = userMapper.toEntity(userDto);
        if (patternMatches(user.getEmail())) {
            throw new RuntimeException("Email is not validate");
        }
        userService.create(user);
    }

    @GetMapping("/users/{userId}/view")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Take a view of user's tickets.",
            description = "This endpoint allows customers "
                    + "to create a view of user's tickets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    EventDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content) })
    @SecurityRequirements({@SecurityRequirement(name = "Bearer") })
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public List<EventDto> findAllByUserId(
                                         @Parameter(description =
                                         "The ID of the user to take a view "
                                                 + "on tickets for.")
                                         @PathVariable
                                         final Long userId) {
        applogger.info("Controller method to look up a user's view of event");
        List<Event> events = eventService.getAllByUserId(userId);
        return eventMapper.toDto(events);
    }

    public static boolean patternMatches(final String emailAddress) {
        return Pattern.compile("^(.+)@(\\\\S+)$")
                .matcher(emailAddress)
                .matches();
    }

}
