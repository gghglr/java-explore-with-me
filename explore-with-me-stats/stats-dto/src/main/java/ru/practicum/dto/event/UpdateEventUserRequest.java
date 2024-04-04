package ru.practicum.dto.event;

import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000)
    private String annotation;
    private int category;
    @Size(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    private StateAction stringAction;
    @Size(min = 3, max = 120)
    private String title;
}
