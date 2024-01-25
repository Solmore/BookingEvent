package org.eventbook.eventbooking.web.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.service.EventService;
import org.eventbook.eventbooking.web.dto.event.EventDto;
import org.eventbook.eventbooking.web.dto.event.EventRequestDto;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.mapper.EventMapper;
import org.eventbook.eventbooking.web.security.JwtTokenProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
@Tag(name = "Events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/events")
    @PreAuthorize("@customSecurityExpression.canAccessEvent(#dto.id)")
    public BigInteger createEvent(@Validated(OnCreate.class)
                                 @RequestBody final EventDto dto) {
        Event event = eventMapper.toEntity(dto);
        eventService.create(event);
        return event.getId();
    }

    @GetMapping("/events")
    @SecurityRequirements({
            @SecurityRequirement(name = "BasicAuthentication"),
            @SecurityRequirement(name = "Bearer") })
    @PreAuthorize("@authentication")
    public List<Event> getAllEventByNameAndDurationAndCategory(
            @Validated(OnCreate.class)
            @RequestBody final EventRequestDto dto) {
        if (dto.getCategory() == null) {
            eventService.getAllEventByNameAndDuration(
                    dto.getName(),
                    dto.getStartDate(),
                    dto.getEndDate()
            );

        }
        return eventService.getAllEventByNameAndDurationAndCategory(
                dto.getName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCategory());
    }


}
