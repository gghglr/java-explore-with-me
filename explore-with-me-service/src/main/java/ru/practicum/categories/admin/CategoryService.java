package ru.practicum.categories.admin;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

public interface CategoryService {

    CategoryDto create(NewCategoryDto newCategoryDto);
    void delete(long catId);
    CategoryDto update(NewCategoryDto newCategoryDto, long catId);
}
