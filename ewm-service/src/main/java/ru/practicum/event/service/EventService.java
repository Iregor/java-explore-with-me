package ru.practicum.event.service;

import ru.practicum.event.data.dto.*;
import ru.practicum.request.data.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto createEvent(NewEventDto newEventDto, long userId);

    EventFullDto findUserEventById(long userId, long eventId);

    EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest updateRequest);

    EventFullDto moderateEvent(long eventId, UpdateEventAdminRequest eventAdminRequest);

    List<ParticipationRequestDto> getAllEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);

    List<EventFullDto> getAllEventWithParams(SearchParameters parameters);

    List<EventShortDto> getAllPublicEventsWithParams(SearchParameters parameters, HttpServletRequest request);

    EventFullDto getEventById(long eventId, HttpServletRequest request);
}