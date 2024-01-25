package org.eventbook.eventbooking.web.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.eventbook.eventbooking.domain.event.Category;

import java.util.Date;

@Data
@Schema(name = "EventRequestDTO")
public class EventRequestDto {

    @Schema(description = "Event name", example = "RENAISSANCE WORLD TOUR")
    private String name;

    @Schema(description = "Date start event", example = "2024-01-25")
    private Date startDate;

    @Schema(description = "Date end event", example = "2024-01-29")
    private Date endDate;

    @Schema(description = "Category", example = "CONCERT")
    private Category category;


}
