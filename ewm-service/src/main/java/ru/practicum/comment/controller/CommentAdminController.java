package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable @Positive long commentId) {
        commentService.deleteCommentById(commentId);
        log.info("{}: comment with id={} was deleted", LocalDateTime.now(), commentId);
    }
}
