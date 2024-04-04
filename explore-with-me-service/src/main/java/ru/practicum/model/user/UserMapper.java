package ru.practicum.model.user;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.user.User;

public class UserMapper {

    public static <T> User toUserFromNewRequest(NewUserRequest userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static UserShortDto toUserShort(User user) {
        return new UserShortDto(user.getId(), user.getEmail());
    }
}
