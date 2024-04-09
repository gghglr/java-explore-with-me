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
import ru.practicum.model.event.UserEvent;
import ru.practicum.model.event.UserEventMapper;

import java.util.*;
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
        List<Long> allIdsEvent = new ArrayList<>();
        compilations.stream().forEach(x -> {
            List<Long> ids = x.getEvents();
            ids.stream().forEach(currentId -> allIdsEvent.add(currentId));
        });

        List<UserEvent> userEvents = eventRepository.findByIdIn(allIdsEvent);
        Map<Long, UserEvent> map = new HashMap<>();
        userEvents.stream().forEach(x -> map.put(x.getId(), x));

        for (Compilations compilation : compilations) {
            List<EventShortDto> shortDtos = new ArrayList<>();
            compilation.getEvents().stream().forEach(x -> {
                shortDtos.add(UserEventMapper.toShortDto(map.get(x)));
            });
            compilationDto.add(CompilationsMapper.toDto(compilation, shortDtos));
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
