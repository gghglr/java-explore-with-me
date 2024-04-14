package ru.practicum.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.mark.MarkDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class MarksController {

    private final MarksService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/event/{eventId}/like")
    public MarkDto createLike(@PathVariable("userId") long userId,
                              @PathVariable("eventId") long eventId) {
        log.info("Получен запрос на создание лайка от {} для ивента {}", userId, eventId);
        return service.createLike(userId, eventId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/event/{eventId}/dislike")
    public MarkDto createDislike(@PathVariable("userId") long userId,
                                 @PathVariable("eventId") long eventId) {
        log.info("Получен запрос на создание дизлайка от {} для ивента {}", userId, eventId);
        return service.createDislike(userId, eventId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/event/{eventId}/dislike")
    public void deleteDislike(@PathVariable("userId") long userId,
                              @PathVariable("eventId") long eventId) {
        log.info("Удаление дизлайка пользователем {} у события {}", userId, eventId);
        service.deleteDislike(userId, eventId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/event/{eventId}/like")
    public void deleteLike(@PathVariable("userId") long userId,
                           @PathVariable("eventId") long eventId) {
        log.info("Удаление лайка пользователем {} у события {}", userId, eventId);
        service.deleteLike(userId, eventId);
    }
}
