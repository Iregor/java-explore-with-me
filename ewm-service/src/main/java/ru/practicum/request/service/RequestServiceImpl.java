package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.data.Event;
import ru.practicum.event.data.EventRepository;
import ru.practicum.event.data.EventState;
import ru.practicum.exception.EventParticipationForbiddenException;
import ru.practicum.request.data.Request;
import ru.practicum.request.data.RequestRepository;
import ru.practicum.request.data.RequestStatus;
import ru.practicum.request.data.dto.ParticipationRequestDto;
import ru.practicum.request.data.dto.RequestMapper;
import ru.practicum.user.data.User;
import ru.practicum.user.data.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        User requestor = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        validateRequestorNotInitiator(event, requestor);
        validateEventIsPublished(event);
        validateParticipationLimitNotReached(event);
        Request request = Request.builder()
                .requestor(requestor)
                .event(event)
                .status(event.getRequestModeration() && event.getParticipantLimit() != 0 ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .created(LocalDateTime.now())
                .build();
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> getAllUserRequests(long userId) {
        return requestRepository.findAllByRequestorId(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Request request = requestRepository.getByIdAndRequestorId(requestId, userId).orElseThrow();
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private void validateRequestorNotInitiator(Event event, User requestor) {
        if (event.getInitiator().getId().equals(requestor.getId())) {
            throw new EventParticipationForbiddenException("Initiator can not be requestor himself.");
        }
    }

    private void validateEventIsPublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventParticipationForbiddenException("Event is not published.");
        }
    }

    private void validateParticipationLimitNotReached(Event event) {
        long acceptedRequestsCount = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, event.getId());
        if (acceptedRequestsCount >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new EventParticipationForbiddenException("Event participation limit reached.");
        }
    }
}