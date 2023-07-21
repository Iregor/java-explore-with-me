package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.data.dto.UserDto;
import ru.practicum.user.service.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserAdminController {
    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        UserDto dto = userService.createUser(userDto);
        log.info(String.format("%s: user was created: %s", LocalDateTime.now(), userDto));
        return dto;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        List<UserDto> users = userService.getUsers(ids, from, size);
        log.info(String.format("%s: users were found for request: ids: %s; from=%d; size=%d", LocalDateTime.now(), ids == null ? "null" : ids.toString(), from, size));
        return users;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable @Positive long userId) {
        userService.deleteUserById(userId);
        log.info(String.format("%s: user was deleted by id: %d", LocalDateTime.now(), userId));
    }

}
