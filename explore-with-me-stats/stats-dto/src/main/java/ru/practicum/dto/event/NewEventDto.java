package ru.practicum.dto.event;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {

    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;
    @NotNull
    private int category;
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    @NotNull
    private Location location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

}
