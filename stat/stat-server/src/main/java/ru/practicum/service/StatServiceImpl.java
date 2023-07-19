package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.exception.DatesNotConsistentException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public void postHit(EndpointHitDto endpointHitDto) {
        statRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        log.info(String.format("Endpoint saved: %s", endpointHitDto));
    }

    @Override
    public List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        validateDates(start, end);
        List<ViewStats> response = unique ?
                statRepository.findViewStatsByUniqueIP(start, end, uris) :
                statRepository.findViewStats(start, end, uris);
        log.info(String.format("Stat for request params is resolved: start = %s, end = %s, uris = %s, unique = %b", start, end, getUrisList(uris), unique));
        return response;
    }

    private String getUrisList(List<String> uris) {
        if (uris == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (String uri : uris) {
            sb.append(uri).append("&");
        }
        return sb.toString();
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new DatesNotConsistentException("Start date must be before end date");
        }
    }
}