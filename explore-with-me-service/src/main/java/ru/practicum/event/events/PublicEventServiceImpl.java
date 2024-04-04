package ru.practicum.event.events;

import jdk.dynalink.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
public class PublicEventServiceImpl implements PublicEventService{

    @Autowired
    private final UserEventRepository repository;

    @Override
    public List<EventFullDto> getEventsForQuery(String text, Boolean paid, boolean onlyAvailable, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, String sort,
                                                int from, int size) {
        Pageable pageable = null;
        if (sort.equals("EVENT_DATE")) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        }
        if (sort.equals("VIEWS")) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "views"));
        }
        if (rangeStart.isAfter(rangeEnd)) {
           throw new ValidationException("конец события раньше старта");
        }
        if (pageable == null) {
            throw new NotFoundException("Сортировка не найдена");
        }
        List<EventFullDto> eventFullDto = new ArrayList<>();
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
    public EventFullDto getById(long id) {
        Optional<UserEvent> event = repository.findById(id);
        if(event.isEmpty()){
            throw new NotFoundException("Событие не найдено");
        }
//        if(!event.get().getState().equals(State.PUBLISHED)) {
//            throw new NotFoundException("Это событие еще не опубликовано");
//        }
        return UserEventMapper.toEventDtoFromEvent(event.get());
    }
}
