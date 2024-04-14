package ru.practicum.model.event;

import ru.practicum.dto.event.*;
import ru.practicum.model.categories.CategoryMapper;
import ru.practicum.model.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserEventMapper {

    public static UserEvent toUserEvent(NewEventDto newEventDto) {
        UserEvent userEvent = new UserEvent();
        userEvent.setAnnotation(newEventDto.getAnnotation());
        userEvent.setDescription(newEventDto.getDescription());
        userEvent.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userEvent.setLat(newEventDto.getLocation().getLat());
        userEvent.setLon(newEventDto.getLocation().getLon());
        userEvent.setPaid(newEventDto.isPaid());
        userEvent.setParticipantLimit(newEventDto.getParticipantLimit());
        userEvent.setRequestModeration(newEventDto.isRequestModeration());
        userEvent.setTitle(newEventDto.getTitle());
        return userEvent;
    }

    public static EventFullDto toEventDtoFromEvent(UserEvent userEvent) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(userEvent.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toDtoFromCategory(userEvent.getCategory()));
        eventFullDto.setConfirmedRequests(userEvent.getConfirmedRequests());
        eventFullDto.setCreatedOn(userEvent.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        eventFullDto.setDescription(userEvent.getDescription());
        eventFullDto.setEventDate(userEvent.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        eventFullDto.setId(userEvent.getId());
        eventFullDto.setInitiator(UserMapper.toUserShort(userEvent.getInitiator()));
        eventFullDto.setLocation(new Location(userEvent.getLat(), userEvent.getLon()));
        eventFullDto.setPaid(userEvent.isPaid());
        eventFullDto.setParticipantLimit(userEvent.getParticipantLimit());
        if (userEvent.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(userEvent.getPublishedOn().format(DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            eventFullDto.setPublishedOn(null);
        }
        eventFullDto.setRequestModeration(userEvent.isRequestModeration());
        eventFullDto.setState(userEvent.getState());
        eventFullDto.setTitle(userEvent.getTitle());
        eventFullDto.setViews(userEvent.getViews());
        eventFullDto.setLikes(userEvent.getLikes());
        eventFullDto.setDislikes(userEvent.getDislikes());
        eventFullDto.setRating(userEvent.getRating());
        return eventFullDto;
    }

    public static EventShortDto toShortDto(UserEvent userEvent) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(userEvent.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toDtoFromCategory(userEvent.getCategory()));
        eventShortDto.setConfirmedRequests(userEvent.getConfirmedRequests());
        eventShortDto.setEventDate(userEvent.getEventDate());
        eventShortDto.setId(userEvent.getId());
        eventShortDto.setInitiator(UserMapper.toUserShort(userEvent.getInitiator()));
        eventShortDto.setPaid(userEvent.isPaid());
        eventShortDto.setTitle(userEvent.getTitle());
        eventShortDto.setViews(userEvent.getViews());
        return eventShortDto;
    }
}
