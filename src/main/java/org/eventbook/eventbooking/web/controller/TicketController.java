package org.eventbook.eventbooking.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eventbook.eventbooking.service.TicketService;
import org.eventbook.eventbooking.service.UserService;
import org.eventbook.eventbooking.web.dto.validation.OnUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Tag(name = "Tickets")
@Slf4j
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final Logger applogger = LoggerFactory
            .getLogger("AppenderLog");
    private final Logger auditlogger =
            LoggerFactory.getLogger("AuditLog");



    @PostMapping("/events/{eventId}/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Reserve tickets for an event.",
            description = "This endpoint allows customers "
                    + "to reserve tickets for an event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)})
    @SecurityRequirements({@SecurityRequirement(name = "Bearer") })
    public Long reserve(@Parameter(description =
                        "The ID of the event to reserve tickets for.")
                        @Validated(OnUpdate.class)
                        @PathVariable
                        final Long eventId,
                        @Parameter(description =
                                "The ticket reservation request.")
                        @RequestBody
                        final BigInteger ticketReserve) {
        applogger.info("TicketController to book a ticket to eventId={}",
                eventId);
        Long userId = ticketService.getUserIdByEventId(eventId);
        BigInteger countOnEvent = ticketService.getCountById(eventId);
        countOnEvent = countOnEvent.subtract(ticketReserve);
        if (countOnEvent.signum() == -1) {
            throw new RuntimeException("Can't reserve this count of tickets");
        }
        if (userId.equals(0L)) {
            ticketService.createTicketByUserIdAndEventId(eventId,
                                                         ticketReserve);
            userId = ticketService.getUserIdByEventId(eventId);
            auditlogger.info("TicketController to book a ticket to userId={}",
                    userId);
            return ticketService.uploadCountByEventId(eventId,
                    countOnEvent).getId();
        } else {
            BigInteger countOnUserEvent = ticketService
                    .getCountByUserIdAndEventId(userId, eventId);
            countOnUserEvent = countOnUserEvent.add(ticketReserve);
            ticketService
                    .uploadCountByUserIdAndEventId(
                            userId,
                            eventId,
                            countOnUserEvent);
            return ticketService
                    .uploadCountByEventId(eventId, countOnEvent).getId();
        }
    }

    @PostMapping("/events/{eventId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reserve tickets for an event.",
            description = "This endpoint allows customers "
                    + "to reserve tickets for an event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)})
    @SecurityRequirements({@SecurityRequirement(name = "Bearer") })
    public Long cancel(@Validated(OnUpdate.class)
                       @PathVariable final Long eventId,
                       @RequestBody final BigInteger ticketCancel) {
        applogger.info("Controller method to cancel reserve of event");
        Long userId = ticketService.getUserIdByEventId(eventId);
        if (userId.equals(0L)) {
            applogger.warn("User not reserve to event");
            throw new RuntimeException("User not reserve to Event");
        } else {
            BigInteger countOnEvent = ticketService.getCountById(eventId);
            if (countOnEvent.signum() == 0) {
               applogger.info("End tickets to eventId={}", eventId);
               ticketService.deleteTicketByUserIdAndEventId(userId, eventId);
               throw new RuntimeException("User not reserve to Event");
            }
            countOnEvent = countOnEvent.add(ticketCancel);
            BigInteger countOnUserEvent = ticketService
                    .getCountByUserIdAndEventId(userId, eventId);
            countOnUserEvent = countOnUserEvent.subtract(ticketCancel);
            ticketService.uploadCountByUserIdAndEventId(
                    userId,
                    eventId,
                    countOnUserEvent);
            return ticketService
                    .uploadCountByEventId(eventId, countOnEvent).getId();
        }
    }
}
