package ru.practicum.category.service;

import ru.practicum.category.data.dto.CategoryDto;
import ru.practicum.category.data.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto patchCategory(CategoryDto categoryDto, long catId);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto findCategoryById(long catId);
}
