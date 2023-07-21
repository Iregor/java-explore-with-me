package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.data.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        List<CategoryDto> categories = categoryService.getAllCategories(from, size);
        log.info(String.format("%s: categories are returned: %s", LocalDateTime.now(), categories.toString()));
        return categories;
    }

    @GetMapping("/{catId}")
    CategoryDto findCategoryById(@PathVariable @Positive long catId) {
        CategoryDto category = categoryService.findCategoryById(catId);
        log.info(String.format("%s: category is returned: %s", LocalDateTime.now(), category.toString()));
        return category;

    }
}
