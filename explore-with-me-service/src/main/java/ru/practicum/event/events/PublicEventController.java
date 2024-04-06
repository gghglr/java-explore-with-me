package ru.practicum.event.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.StateAction;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService service;
    private final StatsClient client;

    @Autowired
    public PublicEventController(PublicEventService service, StatsClient client) {
        this.service = service;
        this.client = client;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(name = "text", defaultValue = "") String text,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                        @RequestParam(name = "paid", required = false) Boolean paid,
                                        @RequestParam(name = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                        @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
                                        @RequestParam(name = "rangeStart",
                                                defaultValue = "#{T(java.time.LocalDateTime).now()}")
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(name = "rangeEnd", defaultValue = "2500-01-01 00:00:00")
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(name = "sort", defaultValue = "VIEWS") String sort,
                                        HttpServletRequest httpServletRequest) {
        log.info("Получение событий для неавторизованного пользователя: ");
//        EndpointHitDto endpointHitsDto = new EndpointHitDto();
//        endpointHitsDto.setIp(httpServletRequest.getRemoteAddr());
//        endpointHitsDto.setApp("ewm-main-service");
//        endpointHitsDto.setUri(httpServletRequest.getRequestURI());
//        endpointHitsDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        client.makeAndDoPostRequest("http://localhost:9090/hit", endpointHitsDto);
        return service.getEventsForQuery(text, paid, onlyAvailable, categories, rangeStart, rangeEnd, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable("id") long id) {
        log.info("Публичный запрос: получение событие по id {}", id);
        return service.getById(id);
    }

}
