package ru.practicum.event.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(long initiatorId, Pageable page);

    Optional<Event> findByInitiatorIdAndId(long initiatorId, long eventId);

    Optional<Event> findByIdAndState(long eventId, EventState eventState);

    List<Event> findAllByIdIn(List<Long> eventIds);
}
