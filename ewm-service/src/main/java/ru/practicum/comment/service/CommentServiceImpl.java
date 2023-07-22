package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.comment.data.Comment;
import ru.practicum.comment.data.CommentMapper;
import ru.practicum.comment.data.CommentRepository;
import ru.practicum.comment.data.dto.CommentDto;
import ru.practicum.comment.data.dto.CommentPatchDto;
import ru.practicum.comment.data.dto.NewCommentDto;
import ru.practicum.event.data.Event;
import ru.practicum.event.data.EventRepository;
import ru.practicum.user.data.User;
import ru.practicum.user.data.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto createComment(long userId, long eventId, NewCommentDto newCommentDto) {
        User commentator = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        Comment comment = CommentMapper.toComment(commentator, event, newCommentDto);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(long userId, long eventId, CommentPatchDto commentPatchDto) {
        Comment comment = commentRepository.findByCommentatorIdAndEventId(userId, eventId).orElseThrow();
        patchComment(comment, commentPatchDto);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(long userId, long eventId, long commentId) {
        Comment comment = commentRepository.findByIdAndCommentatorIdAndEventId(commentId, userId, eventId).orElseThrow();
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto getCommentById(long eventId, long commentId) {
        return CommentMapper.toCommentDto(commentRepository.findByIdAndEventId(commentId, eventId).orElseThrow());
    }

    @Override
    public List<CommentDto> getUserComments(long userId, int from, int size) {
        Pageable page = getPageable(from, size);
        return commentRepository.findAllByCommentatorId(userId, page)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getEventComments(long eventId, int from, int size) {
        Pageable page = getPageable(from, size);
        return commentRepository.findAllByEventId(eventId, page)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size, Sort.by("id").ascending());
    }

    private void patchComment(Comment comment, CommentPatchDto commentPatchDto) {
        if (commentPatchDto != null) {
            comment.setText(commentPatchDto.getText());
        }
    }
}
