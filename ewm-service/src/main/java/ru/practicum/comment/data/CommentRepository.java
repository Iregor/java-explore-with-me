package ru.practicum.comment.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentatorIdAndEventId(long userId, long eventId);

    Optional<Comment> findByIdAndCommentatorIdAndEventId(long commentId, long userId, long eventId);

    Optional<Comment> findByIdAndEventId(long commentId, long eventId);

    List<Comment> findAllByCommentatorId(long userId, Pageable page);

    List<Comment> findAllByEventId(long eventId, Pageable page);

    long countByEventId(long eventId);
}
