package ru.practicum.categories.users;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoriesService {

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(long catId);
}
