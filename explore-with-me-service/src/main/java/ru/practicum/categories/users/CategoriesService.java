package ru.practicum.categories.users;

import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoriesService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(long catId);
}
