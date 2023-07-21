package ru.practicum.event.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.event.data.UserEventStateAction;
import ru.practicum.validation.InFuture;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {
    private UserEventStateAction stateAction;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @InFuture(2)
    private LocalDateTime eventDate;
}
