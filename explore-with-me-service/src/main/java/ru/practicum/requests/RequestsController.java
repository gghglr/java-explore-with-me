package ru.practicum.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class RequestsController {

    private final RequestsService service;

    @Autowired
    public RequestsController(RequestsServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable(name = "userId") long userId,
                                          @RequestParam(name = "eventId") long eventId) {
        log.info("Запрос на создание заявки от пользователя {} на событие {}", userId, eventId);
        return service.create(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable(name = "userId") long userId) {
        log.info("Получен запрос от {} на получение всех заявок для событий", userId);
        return service.getRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(name = "userId") long userId,
                                                 @PathVariable(name = "requestId") long requestId) {
        log.info("Отмена заявки пользователем {} от события {}", userId, requestId);
        return service.cancelRequest(userId, requestId);
    }

}
