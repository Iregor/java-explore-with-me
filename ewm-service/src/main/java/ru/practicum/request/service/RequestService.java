package ru.practicum.request.service;

import ru.practicum.request.data.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(long userId, long eventId);

    List<ParticipationRequestDto> getAllUserRequests(long userId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
