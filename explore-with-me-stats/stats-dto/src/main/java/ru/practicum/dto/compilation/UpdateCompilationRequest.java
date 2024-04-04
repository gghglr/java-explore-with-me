package ru.practicum.dto.compilation;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {

    private List<Long> events;
    private boolean pinned;
    private String title;
}
