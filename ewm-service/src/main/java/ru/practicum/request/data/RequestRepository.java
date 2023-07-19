package ru.practicum.request.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    long countByStatusAndEventId(RequestStatus status, long eventId);

    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByIdIn(List<Long> ids);

    List<Request> findAllByRequestorId(long userId);

    Optional<Request> getByIdAndRequestorId(long requestId, long userId);


}
