package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.data.dto.CommentDto;
import ru.practicum.comment.data.dto.CommentPatchDto;
import ru.practicum.comment.data.dto.NewCommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    public CommentDto createComment(@PathVariable @Positive long userId, @PathVariable @Positive long eventId, @RequestBody @Valid NewCommentDto newCommentDto) {
        CommentDto comment = commentService.createComment(userId, eventId, newCommentDto);
        log.info("{}: comment created: {}", LocalDateTime.now(), comment);
        return comment;
    }

    @PatchMapping("/events/{eventId}/comments")
    public CommentDto updateComment(@PathVariable @Positive long userId, @PathVariable @Positive long eventId, @RequestBody @Valid CommentPatchDto commentPatchDto) {
        CommentDto comment = commentService.updateComment(userId, eventId, commentPatchDto);
        log.info("{}: comment updated: {}", LocalDateTime.now(), comment);
        return comment;
    }

    @DeleteMapping("/events/{eventId}/comments/{commentId}")
    public void deleteCommentById(@PathVariable @Positive long userId, @PathVariable @Positive long eventId, @PathVariable @Positive long commentId) {
        commentService.deleteComment(userId, eventId, commentId);
        log.info("{}: comment deleted: userId = {}, eventId = {}, commentId = {}", LocalDateTime.now(), userId, eventId, commentId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getUserComments(@PathVariable @Positive long userId, @RequestParam @PositiveOrZero int from, @RequestParam @Positive int size) {
        List<CommentDto> comments = commentService.getUserComments(userId, from, size);
        log.info("{}: comments returned: {}", LocalDateTime.now(), comments);
        return comments;
    }
}
