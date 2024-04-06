package ru.practicum.event.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.categories.admin.CategoryRepository;
import ru.practicum.dto.event.*;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.event.UserEvent;
import ru.practicum.model.event.UserEventMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final UserEventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users,
                                                List<State> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                int from,
                                                int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<UserEvent> events;
        if (users.size() == 0) {
            if (states.size() == 0) {
                if (categories.size() == 0) {
                    events = eventRepository.getForAdminWithoutParam(rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getForAdminWithCategories(categories, rangeStart, rangeEnd, page).getContent();
                }
            } else {
                if (categories.size() == 0) {
                    events = eventRepository.getForAdminStates(states, rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getForAdminCategoriesAndStates(categories, states, rangeStart, rangeEnd, page).getContent();
                }
            }
        } else {
            if (states.size() == 0) {
                if (categories.size() == 0) {
                    events = eventRepository.getForAdminUsers(users, rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.getForAdminUsersCategories(users, categories, rangeStart, rangeEnd, page).getContent();
                }
            } else {
                if (categories.size() == 0) {
                    events = eventRepository.getForAdminUsersStates(users, states, rangeStart, rangeEnd, page).getContent();
                } else {
                    events = eventRepository.findByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBetween(users, states, categories, rangeStart, rangeEnd, page).getContent();
                }
            }
        }
        return events.stream().map(UserEventMapper::toEventDtoFromEvent).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(UpdateEventAdminRequest eventAdminDto, long eventId) {
        Optional<UserEvent> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new NotFoundException("событие не найдено");
        }
        UserEvent event = eventOpt.get();
        if (eventAdminDto.getStateAction() != null) {
            if (event.getState().equals(State.PENDING) && eventAdminDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                UserEvent eventUpdate = currentUpdate(event, eventAdminDto);
                return UserEventMapper.toEventDtoFromEvent(eventRepository.save(eventUpdate));
            }
            if (event.getState().equals(State.PENDING) && eventAdminDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                event.setPublishedOn(LocalDateTime.now());
                event.setState(State.CANCELED);
                UserEvent eventUpdate = currentUpdate(event, eventAdminDto);
                return UserEventMapper.toEventDtoFromEvent(eventRepository.save(eventUpdate));
            }
            if (eventAdminDto.getStateAction().equals(StateAction.PUBLISH_EVENT) && event.getState().equals(State.CANCELED)) {
                throw new ConflictException("Событие должно иметь State.PENDING");
            }
            if (event.getState().equals(State.PUBLISHED)) {
                throw new ConflictException("Событие не должно быть PUBLISHED");
            }
        }
        UserEvent eventUpdate = currentUpdate(event, eventAdminDto);
        return UserEventMapper.toEventDtoFromEvent(eventRepository.save(eventUpdate));
    }

    private UserEvent currentUpdate(UserEvent event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).get());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null ) {
            LocalDateTime timeToUpdate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(timeToUpdate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата уже наступила");
            }
            event.setEventDate(timeToUpdate);
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getParticipantLimit() > 0) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return event;
    }
}
