package ru.practicum.categories.users;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

@RestController
@Slf4j
public class CategoriesController {

    private final CategoriesService service;

    @Autowired
    public CategoriesController(CategoriesServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получение всех категорий с {} и количество: {}", from, size);
        return service.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable(name = "catId") long catId) {
        log.info("Получение всех категории с id {}",catId);
        return service.getCategoryById(catId);
    }

}
