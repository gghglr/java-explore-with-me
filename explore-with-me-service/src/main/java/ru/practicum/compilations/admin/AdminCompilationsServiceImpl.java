package ru.practicum.compilations.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilations.Compilations;
import ru.practicum.model.compilations.CompilationsMapper;
import ru.practicum.model.event.UserEventMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {

    @Autowired
    private final AdminCompilationsRepository repository;
    @Autowired
    private final UserEventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilations compilations = CompilationsMapper.toCompilations(newCompilationDto);
        repository.save(compilations);
        List<EventShortDto> events = eventRepository.findByIdIn(compilations.getEvents()).stream()
                .map(UserEventMapper::toShortDto)
                .collect(Collectors.toList());
        CompilationDto compilationDto = CompilationsMapper.toDto(compilations);
        compilationDto.setEvents(events);
        return compilationDto;
    }

    @Override
    public void delete(long compId) {
        checkCompExist(repository.findById(compId));
        repository.deleteById(compId);
    }

    @Override
    public CompilationDto update(UpdateCompilationRequest update, long compId) {
        Optional<Compilations> compilationsOpt = repository.findById(compId);
        checkCompExist(compilationsOpt);
        Compilations compilations = compilationsOpt.get();
        if(!update.getEvents().isEmpty() && update.getEvents() != compilations.getEvents()) {
            compilations.setEvents(update.getEvents());
        }
        if((Boolean)update.isPinned() != null && update.isPinned() != compilations.isPinned()) {
            compilations.setPinned(update.isPinned());
        }
        if(!update.getTitle().isEmpty() && !update.getTitle().equals(compilations.getTitle())){
            compilations.setTitle(update.getTitle());
        }
        repository.save(compilations);
        List<EventShortDto> events = eventRepository.findByIdIn(compilations.getEvents()).stream()
                .map(UserEventMapper::toShortDto)
                .collect(Collectors.toList());
        CompilationDto compilationDto = CompilationsMapper.toDto(compilations);
        compilationDto.setEvents(events);
        return compilationDto;
    }

    private void checkCompExist(Optional<Compilations> compilations) {
        if(compilations.isEmpty()) {
            throw new NotFoundException("Нечего удалять");
        }
    }
}
