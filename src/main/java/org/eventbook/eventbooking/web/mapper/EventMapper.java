package org.eventbook.eventbooking.web.mapper;

import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.web.dto.event.EventDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EventMapper extends Mappable<Event, EventDto> {
}
