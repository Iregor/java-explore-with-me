package ru.practicum.request.data.dto;

import ru.practicum.request.data.Request;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequestor().getId())
                .status(request.getStatus())
                .build();
    }
}
