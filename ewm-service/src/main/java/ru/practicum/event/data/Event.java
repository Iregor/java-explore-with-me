package ru.practicum.event.data;

import lombok.*;
import ru.practicum.category.data.Category;
import ru.practicum.user.data.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Event {
    @NotBlank
    private String annotation;
    @NotNull
    @ManyToOne
    private Category category;
    @NotNull
    private LocalDateTime createdOn;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    private User initiator;
    private float lat;
    private float lon;
    @NotNull
    private Boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private LocalDateTime publishedOn;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    @Enumerated(EnumType.STRING)
    private EventState state;
    @NotBlank
    private String title;
}