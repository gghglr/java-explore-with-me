package ru.practicum.users.admin;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface AdminService {

    UserDto save(NewUserRequest user);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void delete(long userId);
}
