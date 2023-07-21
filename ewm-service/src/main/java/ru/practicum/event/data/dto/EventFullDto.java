package ru.practicum.event.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.data.dto.CategoryDto;
import ru.practicum.event.data.EventState;
import ru.practicum.event.data.Location;
import ru.practicum.user.data.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto extends EventShortDto {
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @NotBlank
    private String description;
    @NotNull
    private Location location;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private LocalDateTime publishedOn;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    private EventState state;

    @Builder(builderMethodName = "fullBuilder")
    public EventFullDto(String annotation,
                        CategoryDto category,
                        long confirmedRequests,
                        LocalDateTime eventDate, long id,
                        UserShortDto initiator,
                        Boolean paid,
                        String title,
                        long views,
                        LocalDateTime createdOn,
                        String description,
                        Location location,
                        int participantLimit,
                        LocalDateTime publishedOn,
                        Boolean requestModeration,
                        EventState state) {
        super(annotation, category, confirmedRequests, eventDate, id, initiator, paid, title, views);
        this.createdOn = createdOn;
        this.description = description;
        this.location = location;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
    }
}


