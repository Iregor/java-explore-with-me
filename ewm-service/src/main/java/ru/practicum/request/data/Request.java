package ru.practicum.request.data;

import lombok.*;
import ru.practicum.event.data.Event;
import ru.practicum.user.data.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    private User requestor;
    @NotNull
    @ManyToOne
    private Event event;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @NotNull
    private LocalDateTime created;
}
