package ru.practicum.user.service;

import ru.practicum.user.data.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void deleteUserById(long userId);
}
