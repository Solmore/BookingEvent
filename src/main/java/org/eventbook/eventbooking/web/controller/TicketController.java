package org.eventbook.eventbooking.web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.service.EventService;
import org.eventbook.eventbooking.web.dto.validation.OnUpdate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
@Tag(name = "Tickets")
public class TicketController {

    private final EventService eventService;



    @PostMapping("/events/{eventId}/tickets")
    @SecurityRequirements({
            @SecurityRequirement(name = "BasicAuthentication"),
            @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("@customSecurityExpression.canAccessEvent(#eventId)")
    public BigInteger reserve(@Validated(OnUpdate.class)
                              @PathVariable final BigInteger eventId,
                              @RequestBody final BigInteger ticketReserve) {
        BigInteger count = eventService.getCountById(eventId);
        count = count.add(ticketReserve);
        return eventService.uploadCountByEventId(eventId, count).getId();
    }

    @PostMapping("/events/{eventId}/cancel")
    @SecurityRequirements({
            @SecurityRequirement(name = "BasicAuthentication"),
            @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("@customSecurityExpression.canAccessEvent(#eventId)")
    public BigInteger cancel(@Validated(OnUpdate.class)
                              @PathVariable final BigInteger eventId,
                              @RequestBody final BigInteger ticketReserve) {
        BigInteger count = eventService.getCountById(eventId);
        count = count.subtract(ticketReserve);
        return eventService.uploadCountByEventId(eventId, count).getId();
    }
}
