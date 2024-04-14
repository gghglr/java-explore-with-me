package ru.practicum.event.events;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.State;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.event.UserEvent;
import ru.practicum.model.event.UserEventMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    @Autowired
    private final UserEventRepository repository;
    private final StatsClient client;

    @Override
    public List<EventFullDto> getEventsForQuery(String text, Boolean paid, boolean onlyAvailable, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, String sort,
                                                int from, int size) {
        Pageable pageable;
        if (sort.equals("EVENT_DATE") || sort.equals("VIEWS") || sort.equals("RATING")) {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, toLowerCase(sort)));
        } else {
            throw new NotFoundException("Сортировка не найдена");
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("конец события раньше старта");
        }

        List<UserEvent> events;
        if (onlyAvailable) {
            if (categories.size() == 0) {
                events = repository.getEventsTrueWithoutCategories(text, paid, rangeStart, rangeEnd, pageable)
                        .getContent();
            } else {
                events = repository.getEventsTrueWithCategories(text, paid, categories, rangeStart, rangeEnd, pageable)
                        .getContent();
            }
        } else {
            if (categories.size() == 0) {
                events = repository.getEventsFalseWithoutCategories(text, paid, rangeStart, rangeEnd, pageable)
                        .getContent();
            } else {
                events = repository.getEventsFalseWithCategories(text, paid, categories, rangeStart, rangeEnd, pageable)
                        .getContent();
            }
        }
        return events.stream().map(UserEventMapper::toEventDtoFromEvent).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getById(long id, String uri, String ip) {
        Optional<UserEvent> event = repository.findById(id);
        if (event.isEmpty()) {
            throw new NotFoundException("Событие не найдено");
        }
        if (!event.get().getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Это событие еще не опубликовано");
        }
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        List<ViewStatsDto> viewStatsDtos = client.getAllStats(LocalDateTime.now().minusYears(10),
                LocalDateTime.now().plusYears(10), uris, true).getBody();
        event.get().setViews(viewStatsDtos.get(0).getHits());
        repository.save(event.get());
        return UserEventMapper.toEventDtoFromEvent(event.get());
    }

    private String toLowerCase(String text) {
        StringBuilder builder = new StringBuilder(text.toLowerCase());
        while (builder.indexOf("_") > 0) {
            int pos = builder.indexOf("_");
            builder.replace(pos, pos + 1, "");
            String letter = builder.substring(pos, pos + 1);
            builder.replace(pos, pos + 1, letter.toUpperCase());
            pos = 0;
        }
        return builder.toString();
    }
}
