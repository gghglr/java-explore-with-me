package ru.practicum.event.users;

import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface UserEventService {

    EventFullDto createEvent(NewEventDto newEventDto, long userId);
    List<EventFullDto> getEventForCurrentUser(long userId, int from, int size);
    EventFullDto getCurrentEvent(long userId, long eventId);
    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);
    List<ParticipationRequestDto> getRequests(long userId, long eventId);
    EventRequestStatusUpdateResult updateEventRequest(long userId, long eventId, EventRequestStatusUpdateRequest request);
}
