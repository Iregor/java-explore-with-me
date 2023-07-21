package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.data.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.data.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable @Positive long userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        List<EventShortDto> userEvents = eventService.getUserEvents(userId, from, size);
        log.info(String.format("%s: events by user id=%d were returned: %s", LocalDateTime.now(), userId, userEvents.toString()));
        return userEvents;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@RequestBody @Valid NewEventDto newEventDto, @PathVariable @Positive long userId) {
        EventFullDto eventDto = eventService.createEvent(newEventDto, userId);
        log.info(String.format("%s: new event is created: %s", LocalDateTime.now(), eventDto.toString()));
        return eventDto;
    }

    @GetMapping("/{eventId}")
    public EventFullDto findUserEventById(@PathVariable @Positive long userId, @PathVariable @Positive long eventId) {
        EventFullDto eventDto = eventService.findUserEventById(userId, eventId);
        log.info(String.format("%s: event by id returned: %s", LocalDateTime.now(), eventDto.toString()));
        return eventDto;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable @Positive long userId,
                                   @PathVariable @Positive long eventId,
                                   @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        EventFullDto eventDto = eventService.patchEvent(userId, eventId, updateRequest);
        log.info(String.format("%s: event was successfully patched: %s", LocalDateTime.now(), eventDto.toString()));
        return eventDto;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllEventRequests(@PathVariable @Positive long userId, @PathVariable @Positive long eventId) {
        List<ParticipationRequestDto> eventRequests = eventService.getAllEventRequests(userId, eventId);
        log.info(String.format("%s: all event requests returned: %s", LocalDateTime.now(), eventRequests.toString()));
        return eventRequests;
    }


    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable @Positive long userId,
                                                               @PathVariable @Positive long eventId,
                                                               @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        EventRequestStatusUpdateResult result = eventService.updateRequestsStatus(userId, eventId, updateRequest);
        log.info(String.format("%s: Requests status updated: %s", LocalDateTime.now(), result.toString()));
        return result;
    }
}
