package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.dto.request.Status;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    List<Long> requestIds;
    Status status;
}
