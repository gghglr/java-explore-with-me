package ru.practicum.model.compilations;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;

public class CompilationsMapper {

    public static Compilations toCompilations (NewCompilationDto newDto) {
        Compilations compilations = new Compilations();
        compilations.setEvents(newDto.getEvents());
        compilations.setPinned(newDto.isPinned());
        compilations.setTitle(newDto.getTitle());
        return compilations;
    }

    public static CompilationDto toDto(Compilations compilations) {
        CompilationDto compilationDto = new CompilationDto();
        compilations.setEvents(compilations.getEvents());
        compilationDto.setId(compilations.getId());
        compilationDto.setPinned(compilations.isPinned());
        compilationDto.setTitle(compilations.getTitle());
        return compilationDto;
    }
}
