package ru.practicum.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "endpoint_hits")
@Getter
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    @Column(name = "app")
    private String app;
    @NotBlank
    @Column(name = "uri")
    private String uri;
    @NotBlank
    @Column(name = "ip")
    private String ip;
    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}