package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void postHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}