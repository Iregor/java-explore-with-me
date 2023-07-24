package ru.practicum.comment.data;

import lombok.*;
import ru.practicum.event.data.Event;
import ru.practicum.user.data.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 5, max = 5000)
    private String text;
    @NotNull
    private LocalDateTime created;
    private LocalDateTime editedOn;
    @NotNull
    @ManyToOne
    private User commentator;
    @NotNull
    @ManyToOne
    private Event event;
}
