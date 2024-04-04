package ru.practicum.model.request;

import ru.practicum.dto.request.ParticipationRequestDto;

public class RequestMapper {

    public static ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setCreated(request.getCreated());
        dto.setEvent((int) request.getEvent().getId());
        dto.setId(request.getId());
        dto.setRequester(request.getRequester());
        dto.setStatus(request.getStatus());
        return dto;
    }
}
