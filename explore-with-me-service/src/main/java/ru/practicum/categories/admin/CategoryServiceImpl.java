package ru.practicum.categories.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.categories.Category;
import ru.practicum.model.categories.CategoryMapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.AlreadyExist;
import ru.practicum.exception.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        validForExistCategory(newCategoryDto.getName());
        return CategoryMapper.toDtoFromCategory(categoryRepository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void delete(long catId) {
        categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Такой категории не существует"));
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(NewCategoryDto newCategoryDto, long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Такой категории не существует"));
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toDtoFromCategory(categoryRepository.save(category));
    }

    private void validForExistCategory(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if(category.isPresent()) {
            throw new AlreadyExist("Такая категория уже есть");
        }
    }
}
