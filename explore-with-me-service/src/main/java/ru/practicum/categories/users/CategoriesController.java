package ru.practicum.categories.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService service;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получение всех категорий с {} и количество: {}", from, size);
        Pageable pageable = PageRequest.of(from, size);
        return service.getCategories(pageable);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") long catId) {
        log.info("Получение всех категории с id {}", catId);
        return service.getCategoryById(catId);
    }

}
