package ru.practicum.event.events;

import ru.practicum.dto.event.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    List<EventFullDto> getEventsForQuery(String text, Boolean paid, boolean onlyAvailable, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, String sort, int from,
                                         int size);

    EventFullDto getById(long id, String uri, String ip);
}
