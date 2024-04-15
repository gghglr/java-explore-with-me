package ru.practicum.categories.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

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
