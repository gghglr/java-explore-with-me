package ru.practicum.dto.event;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class NewEventDto {

    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @NotNull
    private int category;
    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;
    private String eventDate;
    @NotNull
    private Location location;
    private boolean paid = false;
    @PositiveOrZero
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

}
