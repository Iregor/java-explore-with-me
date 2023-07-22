package ru.practicum.request.data.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.request.data.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
