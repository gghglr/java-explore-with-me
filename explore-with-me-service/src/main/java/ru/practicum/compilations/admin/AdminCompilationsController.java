package ru.practicum.compilations.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationsService service;

    @Autowired
    public AdminCompilationsController(AdminCompilationsServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Validated @RequestBody NewCompilationDto compilationDto) {
        log.info("Создание подборки админом: ");
        return service.create(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("compId") long compId) {
        log.info("Получен запрос на удаление подборки с id {}", compId);
        service.delete(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@Validated @RequestBody UpdateCompilationRequest update,
                                 @PathVariable("compId") long compId) {
        log.info("Получен запрос на обновление подборки с id {}", compId);
        return service.update(update, compId);
    }
}
