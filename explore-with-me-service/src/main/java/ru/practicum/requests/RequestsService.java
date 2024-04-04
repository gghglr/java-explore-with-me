package ru.practicum.requests;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestsService {

    ParticipationRequestDto create(long userId, long eventId);

    List<ParticipationRequestDto> getRequests(long userId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
