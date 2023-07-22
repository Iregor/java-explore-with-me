package ru.practicum.comment.data;

import lombok.*;
import ru.practicum.event.data.Event;
import ru.practicum.user.data.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 5, max = 300)
    private String text;
    @NotNull
    private LocalDateTime created;
    @NotNull
    private User commentator;
    @NotNull
    private Event event;
}
