package ru.practicum.categories.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос на создание категории: {}", newCategoryDto.getName());
        return service.create(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("catId") long catId) {
        log.info("Получен запрос на удаление категории с Id: {}", catId);
        service.delete(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable("catId") long catId,
                              @Validated @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос на обновление категории с Id: {}", catId);
        return service.update(newCategoryDto, catId);
    }


}
