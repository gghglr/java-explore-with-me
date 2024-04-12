package ru.practicum.model.compilations;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

public class CompilationsMapper {

    public static Compilations toCompilations(NewCompilationDto newDto) {
        Compilations compilations = new Compilations();
        compilations.setEvents(newDto.getEvents());
        compilations.setPinned(newDto.isPinned());
        compilations.setTitle(newDto.getTitle());
        return compilations;
    }

    public static CompilationDto toDto(Compilations compilations, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setEvents(events);
        compilationDto.setId(compilations.getId());
        compilationDto.setPinned(compilations.getPinned());
        compilationDto.setTitle(compilations.getTitle());
        return compilationDto;
    }
}
