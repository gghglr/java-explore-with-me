package ru.practicum.request;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.request.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitDto saveRequest(EndpointHitDto endpointHitDto);

    List<ViewStats> getRequests(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
