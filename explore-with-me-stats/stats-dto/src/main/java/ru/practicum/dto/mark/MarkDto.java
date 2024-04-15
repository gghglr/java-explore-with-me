package ru.practicum.dto.mark;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarkDto {

    private String mark;
    private int count;
}
