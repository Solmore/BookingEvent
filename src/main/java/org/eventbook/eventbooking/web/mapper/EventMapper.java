package org.eventbook.eventbooking.web.mapper;

import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.web.dto.event.EventDto;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface EventMapper {



    EventDto toDto(Event entity);

    List<EventDto> toDto(List<Event> entity);

    //@Mapping(source = "users", target = "", ignore = true)
    Event toEntity(EventDto dto);

    //@Mapping(source = "users", target = "", ignore = true)
    List<Event> toEntity(List<EventDto> dtos);
}
