package ru.practicum.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {

    private LocalDateTime created;
    private int event;
    private long id;
    private long requester;
    private Status status;
}
