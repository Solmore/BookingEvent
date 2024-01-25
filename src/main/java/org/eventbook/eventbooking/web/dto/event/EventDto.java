package org.eventbook.eventbooking.web.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.eventbook.eventbooking.domain.event.Category;
import org.eventbook.eventbooking.web.dto.validation.OnCreate;
import org.eventbook.eventbooking.web.dto.validation.OnUpdate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.util.Date;

@Data
@Schema(name = "EventDTO")
public class EventDto {

    private BigInteger id;

    @Schema(description = "Event name", example = "RENAISSANCE WORLD TOUR")
    @NotNull(message = "Event name must be not null.",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 100,
            message = "Title length must be smaller than 100 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Schema(description = "Date event", example = "2024-01-29")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Schema(description = "Number of place", example = "50")
    private BigInteger availableAttendeesCount;

    @Length(max = 500,
            message = "Description length must be smaller than 500 symbols.",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;


    private Category category;


}
