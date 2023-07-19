package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatClient;
import ru.practicum.category.data.Category;
import ru.practicum.category.data.CategoryRepository;
import ru.practicum.dto.ViewStats;
import ru.practicum.event.data.*;
import ru.practicum.event.data.dto.*;
import ru.practicum.exception.ChangeEventStatusForbiddenException;
import ru.practicum.exception.EventParticipationForbiddenException;
import ru.practicum.exception.RequestNotValidException;
import ru.practicum.exception.RequestStatusNotPendingException;
import ru.practicum.request.data.QRequest;
import ru.practicum.request.data.Request;
import ru.practicum.request.data.RequestRepository;
import ru.practicum.request.data.RequestStatus;
import ru.practicum.request.data.dto.ParticipationRequestDto;
import ru.practicum.request.data.dto.RequestMapper;
import ru.practicum.user.data.User;
import ru.practicum.user.data.UserRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;
    private final StatClient statClient;


    @Override
    @Transactional
    public List<EventShortDto> getUserEvents(long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, page)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        fillEventListWithViews(events);
        fillEventListWithConfirmedRequests(events);
        return events;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto newEventDto, long userId) {
        User initiator = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow();
        Event event = EventMapper.toEvent(newEventDto, initiator, category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(event = eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto findUserEventById(long userId, long eventId) {
        return EventMapper.toEventFullDto(
                eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(),
                getConfirmedRequests(eventId),
                getSingleEventViews(eventId));
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest updateRequest) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow();
        patchEventState(event, updateRequest);
        patchEventDate(event, updateRequest);
        patchEventFields(event, updateRequest);
        return EventMapper.toEventFullDto(
                eventRepository.save(event),
                getConfirmedRequests(eventId),
                getSingleEventViews(eventId));
    }

    @Override
    @Transactional
    public EventFullDto moderateEvent(long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        patchEventState(event, updateRequest);
        patchEventDate(event, updateRequest);
        patchEventFields(event, updateRequest);

        return EventMapper.toEventFullDto(
                eventRepository.save(event),
                getConfirmedRequests(eventId),
                getSingleEventViews(eventId));
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> getAllEventRequests(long userId, long eventId) {
        eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow();  //just to validate event exists
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow();
        List<Request> requestsToConfirm = requestRepository.findAllByIdIn(updateRequest.getRequestIds());

        if (updateRequest.getStatus() == RequestStatus.REJECTED) {
            return rejectAll(requestsToConfirm);
        }
        validateParticipationLimitNotReached(event);
        if (!event.getRequestModeration() || updateRequest.getStatus() == RequestStatus.CONFIRMED) {    //if no pre-moderation required - return all as confirmed
            return confirmAllUntilLimitReached(event, requestsToConfirm);
        }
        throw new RequestNotValidException("Request status not valid.");
    }

    @Override
    @Transactional
    public List<EventFullDto> getAllEventWithParams(SearchParameters parameters) {
        Predicate predicate = getCommonPredicate(parameters);
        Pageable page = getPageable(parameters.getFrom(), parameters.getSize());
        List<EventFullDto> events = eventRepository.findAll(predicate, page)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        fillEventListWithConfirmedRequests(events);
        fillEventListWithViews(events);
        return events;
    }

    @Override
    @Transactional
    public List<EventShortDto> getAllPublicEventsWithParams(SearchParameters parameters, HttpServletRequest request) {
        Pageable page = getPageable(parameters.getFrom(), parameters.getSize(), parameters.getSortProperty());
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        statClient.saveEndpointHit("emw-service", "/events", request.getRemoteAddr(), LocalDateTime.now().format(EventMapper.dateTimeFormatter));

        List<EventShortDto> events = eventRepository.findAll(getPublicPredicate(query, parameters), page)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        fillEventListWithConfirmedRequests(events);
        fillEventListWithViews(events);
        if (parameters.getSortProperty() == SortProperty.VIEWS) {
            events.sort(Comparator.comparingLong(EventShortDto::getViews));
        }
        return events;
    }

    @Override
    @Transactional
    public EventFullDto getEventById(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow();
        statClient.saveEndpointHit("emw-service", String.format("/events/%d", eventId), request.getRemoteAddr(), LocalDateTime.now().format(EventMapper.dateTimeFormatter));
        return EventMapper.toEventFullDto(event, getConfirmedRequests(eventId), getSingleEventViews(eventId));
    }


    private Event patchEventFields(Event event, UpdateEventRequest updateRequest) {
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory()).orElseThrow();
            event.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getLocation() != null) {
            event.setLat(updateRequest.getLocation().getLat());
            event.setLon(updateRequest.getLocation().getLon());
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        return event;
    }

    private void patchEventDate(Event event, UpdateEventUserRequest updateRequest) {
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
    }

    private void patchEventDate(Event event, UpdateEventAdminRequest updateRequest) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ChangeEventStatusForbiddenException("Event will start within 1 hour.");
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
    }

    private void patchEventState(Event event, UpdateEventUserRequest updateRequest) {
        EventState eventState = event.getState();
        if (eventState == EventState.PUBLISHED) {
            throw new ChangeEventStatusForbiddenException("Event is already published.");
        }
        UserEventStateAction stateAction;
        if ((stateAction = updateRequest.getStateAction()) != null) {
            event.setState(determineEventState(eventState, stateAction));
        }
    }

    private void patchEventState(Event event, UpdateEventAdminRequest updateRequest) {
        EventState eventState = event.getState();
        AdminEventStateAction stateAction;
        if ((stateAction = updateRequest.getStateAction()) != null) {
            event.setState(determineEventState(eventState, stateAction));
        }
        if (stateAction == AdminEventStateAction.PUBLISH_EVENT) {
            event.setPublishedOn(LocalDateTime.now());  //unreachable if event was published earlier (exception in snippet above)
        }
    }

    private EventState determineEventState(EventState eventState, UserEventStateAction stateAction) {
        switch (stateAction) {
            case SEND_TO_REVIEW:
                if (eventState == EventState.CANCELED || eventState == EventState.PENDING) {
                    return EventState.PENDING;
                }
                break;
            case CANCEL_REVIEW:
                if (eventState == EventState.PENDING) {
                    return EventState.CANCELED;
                }
                break;
        }
        return eventState;
    }

    private EventState determineEventState(EventState eventState, AdminEventStateAction stateAction) {
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (eventState == EventState.PENDING) {
                    return EventState.PUBLISHED;
                } else {
                    throw new ChangeEventStatusForbiddenException("Event is already published or canceled.");
                }
            case REJECT_EVENT:
                if (eventState != EventState.PUBLISHED) {
                    return EventState.CANCELED;
                } else {
                    throw new ChangeEventStatusForbiddenException("Event is already published.");
                }
            default:
                return null; //unreachable
        }
    }

    private void validateParticipationLimitNotReached(Event event) {
        long confirmedRequestsCount = requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, event.getId());
        if (confirmedRequestsCount >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new EventParticipationForbiddenException("Event participation limit reached.");
        }
    }

    private EventRequestStatusUpdateResult confirmAllUntilLimitReached(Event event, List<Request> requestsToConfirm) {
        long confirmedRequestsCount = getConfirmedRequests(event.getId());
        long participationLimit = event.getParticipantLimit();
        int requestPointer = 0;
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        while ((confirmedRequestsCount < participationLimit || participationLimit == 0) && requestPointer < requestsToConfirm.size()) {
            Request request = requestsToConfirm.get(requestPointer);
            validateRequestStatusIsPendingOrDefined(request, RequestStatus.CONFIRMED);
            request.setStatus(RequestStatus.CONFIRMED);
            confirmedRequests.add(request);
            requestPointer++;
            confirmedRequestsCount++;
        }
        while (requestPointer < requestsToConfirm.size()) {
            Request request = requestsToConfirm.get(requestPointer);
            validateRequestStatusIsPendingOrDefined(request, RequestStatus.REJECTED);
            request.setStatus(RequestStatus.REJECTED);
            rejectedRequests.add(request);
        }
        List<Request> requestsToSave = new ArrayList<>();
        requestsToSave.addAll(confirmedRequests);
        requestsToSave.addAll(rejectedRequests);
        requestRepository.saveAll(requestsToSave);
        return new EventRequestStatusUpdateResult(
                confirmedRequests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()),
                rejectedRequests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
    }

    private EventRequestStatusUpdateResult rejectAll(List<Request> requestsToReject) {
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request request : requestsToReject) {
            validateRequestStatusIsPendingOrDefined(request, RequestStatus.REJECTED);
            request.setStatus(RequestStatus.REJECTED);
            rejectedRequests.add(request);
        }
        requestRepository.saveAll(rejectedRequests);
        return new EventRequestStatusUpdateResult(
                new ArrayList<>(),
                rejectedRequests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
    }

    private void validateRequestStatusIsPendingOrDefined(Request request, RequestStatus status) {
        if (request.getStatus() != RequestStatus.PENDING && request.getStatus() != status) {
            throw new RequestStatusNotPendingException(String.format("Request status is %s", request.getStatus().name()));
        }
    }

    private BooleanBuilder getCommonPredicate(SearchParameters parameters) {
        QEvent event = QEvent.event;
        BooleanBuilder predicate = new BooleanBuilder();

        if (parameters.getUsers() != null) {
            predicate.and(event.initiator.id.in(parameters.getUsers()));
        }
        if (parameters.getText() != null) {
            predicate.and(event.annotation.containsIgnoreCase(parameters.getText()).or(event.description.containsIgnoreCase(parameters.getText())));
        }
        if (parameters.getPaid() != null) {
            predicate.and(event.paid.eq(parameters.getPaid()));
        }
        if (parameters.getStates() != null) {
            predicate.and(event.state.in(parameters.getStates()));
        }
        if (parameters.getCategories() != null) {
            predicate.and(event.category.id.in(parameters.getCategories()));
        }
        if (parameters.getRangeStart() != null) {
            predicate.and(event.eventDate.after(parameters.getRangeStart()));
        }
        if (parameters.getRangeEnd() != null) {
            predicate.and(event.eventDate.before(parameters.getRangeEnd()));
        }
        return predicate;
    }

    private BooleanBuilder getPublicPredicate(JPAQuery<?> query, SearchParameters parameters) {
        BooleanBuilder predicate = getCommonPredicate(parameters);
        QEvent event = QEvent.event;

        if (parameters.getOnlyAvailable()) {
            predicate.and(event.id.in(getAvailableEventsIds(query)));
        }

        if (parameters.getRangeStart() == null) {
            predicate.and(event.eventDate.after(LocalDateTime.now()));
        }

        predicate.and(event.state.eq(EventState.PUBLISHED));    //to return only published events
        return predicate;
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size, Sort.by("id").ascending());
    }

    private Pageable getPageable(int from, int size, SortProperty sortProperty) {
        if (sortProperty == null || sortProperty == SortProperty.VIEWS) {
            return getPageable(from, size);
        }
        return PageRequest.of(from / size, size, Sort.by("event_date").ascending());
    }

    List<Long> getAvailableEventsIds(JPAQuery<?> query) {
        QRequest request = QRequest.request;
        NumberExpression<Long> id = request.event.id.as("id");
        NumberExpression<Integer> limit = request.event.participantLimit.as("limit");
        NumberExpression<Long> confirmed = request.status.count().as("confirmed");

        return query.select(id, limit, confirmed)     //get all event ids with participation available
                .from(request)
                .where(request.status.eq(RequestStatus.CONFIRMED))
                .groupBy(id)
                .having(limit.gt(confirmed))
                .fetch()
                .stream()
                .map(tuple -> tuple.get(id))
                .collect(Collectors.toList());
    }

    private void fillEventListWithConfirmedRequests(List<? extends EventShortDto> events) {
        HashMap<Long, EventShortDto> eventsMap = new HashMap<>();
        for (EventShortDto dto : events) {
            eventsMap.put(dto.getId(), dto);
        }

        List<Long> ids = events.stream().map(EventShortDto::getId).collect(Collectors.toList());
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        QRequest request = QRequest.request;
        query
                .select(request.event.id, request.status.count())
                .from(request)
                .where(request.event.id.in(ids).and(request.status.eq(RequestStatus.CONFIRMED)))
                .groupBy(request.event.id)
                .fetch()
                .forEach(tuple -> {
                    long eventId = tuple.get(request.event.id);
                    eventsMap.get(eventId).setConfirmedRequests(tuple.get(request.status.count()));
                });
    }

    private long getConfirmedRequests(long eventId) {
        return requestRepository.countByStatusAndEventId(RequestStatus.CONFIRMED, eventId);
    }

    private void fillEventListWithViews(List<? extends EventShortDto> events) {
        List<Long> eventIds = events.stream().map(EventShortDto::getId).collect(Collectors.toList());
        Map<Long, Long> eventViewsMap = getEventViewsMap(eventIds);
        System.out.println(events);
        for (EventShortDto event : events) {
            Long views = eventViewsMap.get(event.getId());
            event.setViews(views != null ? views : 0);
        }
    }

    private Map<Long, Long> getEventViewsMap(List<Long> eventIds) {
        List<String> uris = eventIds.stream().map(id -> String.format("/events/%d", id)).collect(Collectors.toList());
        ViewStats[] viewStats = statClient.getStat(LocalDateTime.of(2020, 1, 1, 1, 1), LocalDateTime.now(), uris, true).getBody();
        HashMap<Long, Long> eventsViews = new HashMap<>();
        for (ViewStats viewStat : viewStats) {
            long eventId = Long.parseLong(viewStat.getUri().split("/")[2]);
            eventsViews.put(eventId, viewStat.getHits());
        }
        return eventsViews;
    }

    private long getSingleEventViews(long eventId) {
        return getSingleEventViews(eventId, null, null);
    }

    private long getSingleEventViews(long eventId, LocalDateTime start, LocalDateTime end) {
        start = start != null ? start : LocalDateTime.of(2020, 1, 1, 1, 1);
        end = end != null ? end : LocalDateTime.now();
        ViewStats[] viewStats = statClient.getStat(start, end, List.of(String.format("/events/%d", eventId)), true).getBody();
        return viewStats.length > 0 ? viewStats[0].getHits() : 0;
    }
}