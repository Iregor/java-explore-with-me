package ru.practicum.event.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.data.Location;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;
}
