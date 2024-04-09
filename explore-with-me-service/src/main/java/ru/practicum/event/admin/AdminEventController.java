package ru.practicum.event.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.State;
import ru.practicum.dto.event.UpdateEventAdminRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService service;

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "users", defaultValue = "") List<Long> users,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                        @RequestParam(name = "states", defaultValue = "") List<State> states,
                                        @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
                                        @RequestParam(name = "rangeStart", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(name = "rangeEnd", defaultValue = "2100-01-01 00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        log.info("Получение событий с параметрами: = {}, states = {}, categories = {}, rangeStart = {}, " +
                "rangeEnd = {}", users, states, categories, rangeStart, rangeEnd);
        return service.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventById(@Validated @RequestBody UpdateEventAdminRequest eventAdminDto,
                                        @PathVariable("eventId") long eventId) {
        log.info("Обновление события от админа для события {}, обновленные {}", eventId, eventAdminDto);
        return service.updateEvent(eventAdminDto, eventId);
    }


}
