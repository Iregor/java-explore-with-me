package ru.practicum.comment.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.data.dto.EventShortDto;
import ru.practicum.user.data.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    @Positive
    @NotNull
    private Long id;
    @NotBlank
    private String text;
    @NotNull
    private LocalDateTime created;
    private LocalDateTime editedOn;
    @NotNull
    private UserShortDto commentator;
    @NotNull
    private EventShortDto event;
}
