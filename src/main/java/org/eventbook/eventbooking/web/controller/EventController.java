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
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.service.EventService;
import org.eventbook.eventbooking.web.dto.event.EventDto;
import org.eventbook.eventbooking.web.dto.event.EventRequestDto;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.mapper.EventMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Tag(name = "Events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new event.",
            description = "This endpoint allows customers "
                    + "to create a new event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)})
    @SecurityRequirements({@SecurityRequirement(name = "Bearer") })
    public Long createEvent(@Parameter(description = "The event to create.")
                            @Validated(OnCreate.class)
                            @RequestBody final EventDto dto) {
        Event event = eventMapper.toEntity(dto);
        eventService.create(event);
        return event.getId();
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all events or search for events.",
            description = "This endpoint allows customers to retrieve "
                    + "all events or search for events by name, "
                    + "date range or category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation =
                                            EventRequestDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)})
    public List<EventDto> getAllEventByNameAndDurationAndCategory(
            @Validated(OnCreate.class)
            @RequestBody final EventRequestDto dto) {
        List<Event> events = new ArrayList<>();
        events = eventService.getAllEventByNameAndDurationAndCategory(
                dto.getName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCategory().toString());
        System.out.println(events);
        return eventMapper.toDto(events);
    }


}
