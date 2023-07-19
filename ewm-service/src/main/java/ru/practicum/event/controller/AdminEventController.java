package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.data.EventState;
import ru.practicum.event.data.dto.EventFullDto;
import ru.practicum.event.data.dto.SearchParameters;
import ru.practicum.event.data.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    List<EventFullDto> getAllEventWithParams(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<EventState> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {

        SearchParameters parameters = new SearchParameters()
                .setUsers(users)
                .setStates(states)
                .setCategories(categories)
                .setRangeStart(rangeStart)
                .setRangeEnd(rangeEnd)
                .setFrom(from)
                .setSize(size);

        List<EventFullDto> events = eventService.getAllEventWithParams(parameters);
        log.info(String.format("%s: events were returned: %s", LocalDateTime.now(), events.toString()));
        return events;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto moderateEvent(@PathVariable @Positive long eventId,
                                      @RequestBody @Valid UpdateEventAdminRequest eventAdminRequest) {
        EventFullDto eventDto = eventService.moderateEvent(eventId, eventAdminRequest);
        log.info(String.format("%s: event was successfully moderated: %s", LocalDateTime.now(), eventDto.toString()));
        return eventDto;
    }
}