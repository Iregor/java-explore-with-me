package ru.practicum.comment.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comment.data.dto.CommentDto;
import ru.practicum.comment.data.dto.NewCommentDto;
import ru.practicum.event.data.Event;
import ru.practicum.event.data.dto.EventMapper;
import ru.practicum.user.data.User;
import ru.practicum.user.data.dto.UserMapper;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static Comment toComment(User commentator, Event event, NewCommentDto newCommentDto, LocalDateTime created) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .commentator(commentator)
                .event(event)
                .created(created)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .editedOn(comment.getEditedOn())
                .commentator(UserMapper.toUserShortDto(comment.getCommentator()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .build();
    }
}
