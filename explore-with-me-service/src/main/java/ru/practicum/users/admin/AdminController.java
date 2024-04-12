package ru.practicum.users.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Validated @RequestBody NewUserRequest user) {
        log.info("Получен запрос на создание пользователя {}", user.getName());
        return adminService.save(user);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids", defaultValue = "") List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0") int form,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен запрос на получение всех пользователей");
        return adminService.getUsers(ids, form, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUSer(@PathVariable("userId") long userId) {
        log.info("Получен запрос на удаление пользователя с id% {}", userId);
        adminService.delete(userId);
    }


}
