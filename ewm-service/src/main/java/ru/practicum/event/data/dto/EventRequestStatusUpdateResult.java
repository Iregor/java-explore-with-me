package ru.practicum.event.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.request.data.dto.ParticipationRequestDto;

import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
