package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public void deleteCommentById(@PathVariable @Positive long commentId) {
        commentService.deleteCommentById(commentId);
        log.info("{}: comment with id={} was deleted", LocalDateTime.now(), commentId);
    }
}
