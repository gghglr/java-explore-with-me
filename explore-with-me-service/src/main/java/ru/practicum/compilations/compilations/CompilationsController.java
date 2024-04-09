package ru.practicum.compilations.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationsController {

    private final CompilationsService service;

    @GetMapping
    public List<CompilationDto> getComp(@RequestParam(value = "pinned", defaultValue = "false") Boolean pinned,
                                        @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Получен запрос на получение подборок с флагом {}, from {}, size {}", pinned, from, size);
        return service.getComp(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable(value = "compId") Long compId) {
        log.info("Получение подборки по id {}", compId);
        return service.getById(compId);
    }


}
