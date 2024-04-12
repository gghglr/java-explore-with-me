package ru.practicum.users.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository repository;

    @Override
    public UserDto save(NewUserRequest user) {
        log.info("Сохраняемый пользователь: {}", user);
        return UserMapper.toUserDto(repository.save(UserMapper.toUserFromNewRequest(user)));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (ids.isEmpty()) {
            return repository.findAll(PageRequest.of(from, size)).stream()
                    .map(UserMapper::toUserDto).collect(Collectors.toList());
        }
        return repository.findByIdIn(ids, PageRequest.of(from, size)).stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public void delete(long userId) {
        repository.deleteById(userId);
    }
}
