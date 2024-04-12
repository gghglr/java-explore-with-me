package ru.practicum.dto.event;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    private StateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
