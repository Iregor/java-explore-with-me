package ru.practicum.comment.service;

import ru.practicum.comment.data.dto.CommentDto;
import ru.practicum.comment.data.dto.CommentPatchDto;
import ru.practicum.comment.data.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long userId, long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(long userId, long eventId, CommentPatchDto commentPatchDto);

    void deleteComment(long userId, long eventId, long commentId);

    CommentDto getCommentById(long eventId, long commentId);

    List<CommentDto> getUserComments(long userId, int from, int size);

    List<CommentDto> getEventComments(long eventId, int from, int size);

    void deleteCommentById(long commentId);
}
