package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.data.dto.EventFullDto;
import ru.practicum.event.data.dto.EventShortDto;
import ru.practicum.event.data.dto.SearchParameters;
import ru.practicum.event.data.dto.SortProperty;
import ru.practicum.event.service.EventService;
import ru.practicum.validation.DateTimeConsistency;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    @DateTimeConsistency
    List<EventShortDto> getAllPublicEvents(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                           @RequestParam(required = false) SortProperty sortProperty,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size,
                                           HttpServletRequest request) {
        SearchParameters parameters = new SearchParameters()
                .setText(text)
                .setCategories(categories)
                .setPaid(paid)
                .setRangeStart(rangeStart)
                .setRangeEnd(rangeEnd)
                .setOnlyAvailable(onlyAvailable)
                .setSortProperty(sortProperty)
                .setFrom(from)
                .setSize(size);
        List<EventShortDto> events = eventService.getAllPublicEventsWithParams(parameters, request);
        log.info(String.format("%s: events are returned: %s", LocalDateTime.now(), events.toString()));
        return events;
    }

    @GetMapping("/{eventId}")
    EventFullDto getEventById(@PathVariable @Positive long eventId, HttpServletRequest request) {
        EventFullDto event = eventService.getEventById(eventId, request);
        log.info(String.format("%s: event is returned: %s", LocalDateTime.now(), event.toString()));
        return event;
    }
}
