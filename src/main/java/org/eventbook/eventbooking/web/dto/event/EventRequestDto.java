package org.eventbook.eventbooking.web.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.eventbook.eventbooking.domain.event.Category;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.dto.validation.OnUpdate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Schema(name = "EventRequestDTO")
public class EventRequestDto {

    @Schema(description = "Event name", example = "RENAISSANCE WORLD TOUR")
    @Length(max = 100,
            message = "Name Event length must be smaller than 100 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date start event", example = "2024-01-25")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date end event", example = "2024-01-29")
    private LocalDate endDate;

    @Schema(description = "Category", example = "Concert")
    private Category category;


}
