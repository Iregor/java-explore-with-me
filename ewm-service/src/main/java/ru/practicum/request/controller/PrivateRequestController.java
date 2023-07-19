package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.data.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAllUserRequests(@PathVariable @Positive long userId) {
        List<ParticipationRequestDto> userRequests = requestService.getAllUserRequests(userId);
        log.info(String.format("%s: user requests are returned: %s", LocalDateTime.now(), userRequests.toString()));
        return userRequests;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable @Positive long userId, @RequestParam @Positive long eventId) {
        ParticipationRequestDto requestDto = requestService.createRequest(userId, eventId);
        log.info(String.format("%s: request successfully created: %s", LocalDateTime.now(), requestDto.toString()));
        return requestDto;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @Positive long userId, @PathVariable @Positive long requestId) {
        ParticipationRequestDto participationDto = requestService.cancelRequest(userId, requestId);
        log.info(String.format("%s: user request canceled: %s", LocalDateTime.now(), participationDto.toString()));
        return participationDto;
    }


}
