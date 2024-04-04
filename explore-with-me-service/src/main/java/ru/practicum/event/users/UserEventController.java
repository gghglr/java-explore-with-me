package ru.practicum.event.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserEventController {

    private final UserEventService service;

    @Autowired
    public UserEventController(UserEventServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Validated @RequestBody NewEventDto newEventDto,
                                    @PathVariable(name = "userId") long userId) {
        log.info("Запрос на создание ивента от пользователя: {}", userId);
        return service.createEvent(newEventDto, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventFullDto> getEvents(@PathVariable(name = "userId") long userId,
                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                        @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Запрос на получение событий, добавленных пользователем: {}", userId);
        return service.getEventForCurrentUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable(name = "userId") long userId,
                                 @PathVariable(name = "eventId") long eventId) {
        log.info("Получение события с Id: {} от пользователя {}", eventId, userId);
        return service.getCurrentEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "userId") long userId,
                                    @PathVariable(name = "eventId") long eventId,
                                    @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Получение события с Id: {} от пользователя {}", eventId, userId);
        return service.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable(name = "userId") long userId,
                                                     @PathVariable(name = "eventId") long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя {}", userId);
        return  service.getRequests(userId, eventId);
    }
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEvent(@PathVariable(name = "userId") long userId,
                                                            @PathVariable(name = "eventId") long eventId,
                                                            @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Получен запрос на изменение статуса заявки");
        return service.updateEventRequest(userId, eventId, request);
    }
}
