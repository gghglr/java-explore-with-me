package ru.practicum.compilations.admin;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationsService {

    CompilationDto create(NewCompilationDto newCompilationDto);
    void delete(long compId);
    CompilationDto update(UpdateCompilationRequest update, long compId);
}
