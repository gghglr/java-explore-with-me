package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.request.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService service;

    @Autowired
    public StatsController(StatsServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/hit")
    public EndpointHitDto saveRequest(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение запроса");
        return service.saveRequest(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getRequests(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Получение запросов");
        return service.getRequests(start, end, uris, unique);
    }
}
