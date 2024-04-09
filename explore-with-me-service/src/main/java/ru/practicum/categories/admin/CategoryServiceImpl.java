package ru.practicum.categories.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.users.UserEventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.categories.Category;
import ru.practicum.model.categories.CategoryMapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.event.UserEvent;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserEventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        validForExistCategory(newCategoryDto.getName());
        return CategoryMapper.toDtoFromCategory(categoryRepository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void delete(long catId) {
        boolean isExist = categoryRepository.existsById(catId);
        Optional<UserEvent> event = eventRepository.findByCategory_Id(catId);
        if (!event.isEmpty() && isExist) {
            throw new ConflictException("Есть события с такой категорией");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(NewCategoryDto newCategoryDto, long catId) {
        Category oldCategory = categoryRepository.findById(catId).get();
        Optional<Category> findByName = categoryRepository.findByName(newCategoryDto.getName());
        if (!findByName.isEmpty() && findByName.get().getId() == catId) {
            return CategoryMapper.toDtoFromCategory(oldCategory);
        }
        if (!findByName.isEmpty() && findByName.get().getName().equals(newCategoryDto.getName())) {
            throw new ConflictException("Имя занято");
        }
        oldCategory.setName(newCategoryDto.getName());
        return CategoryMapper.toDtoFromCategory(oldCategory);
    }

    private void validForExistCategory(String name) {
        boolean isExist = categoryRepository.existsByName(name);
        if (isExist == true) {
            throw new ConflictException("Такая категория уже есть");
        }
    }
}
