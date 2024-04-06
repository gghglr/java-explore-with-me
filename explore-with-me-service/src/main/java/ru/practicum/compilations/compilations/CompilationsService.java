package ru.practicum.compilations.compilations;

import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationsService {

    List<CompilationDto> getComp(Boolean pinned, int from, int size);
    CompilationDto getById(Long compId);
}
