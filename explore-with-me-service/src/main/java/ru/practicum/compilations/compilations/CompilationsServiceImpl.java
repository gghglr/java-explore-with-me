package ru.practicum.compilations.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.admin.AdminCompilationsRepository;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilations.Compilations;
import ru.practicum.model.compilations.CompilationsMapper;
import ru.practicum.model.event.UserEventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {

    private final AdminCompilationsRepository repository;
    private final UserEventRepository eventRepository;

    @Override
    public List<CompilationDto> getComp(Boolean pinned, int from, int size) {
        List<Compilations> compilations = repository.findByPinned(pinned, PageRequest.of(from, size));
        List<CompilationDto> compilationDto = new ArrayList<>();
        for (Compilations compilation : compilations) {
            List<EventShortDto> shortsDto = eventRepository.findByIdIn(compilation.getEvents()).stream()
                    .map(UserEventMapper::toShortDto)
                    .collect(Collectors.toList());
            compilationDto.add(CompilationsMapper.toDto(compilation, shortsDto));
        }


        return compilationDto;
    }

    @Override
    public CompilationDto getById(Long compId) {
        Optional<Compilations> compilations = repository.findById(compId);
        if (compilations.isEmpty()) {
            throw new NotFoundException("Подборка не найдена");
        }
        List<EventShortDto> events = eventRepository.findByIdIn(compilations.get().getEvents()).stream()
                .map(UserEventMapper::toShortDto)
                .collect(Collectors.toList());
        CompilationDto compilationDto = CompilationsMapper.toDto(compilations.get(), events);
        compilationDto.setEvents(events);
        return compilationDto;
    }
}
