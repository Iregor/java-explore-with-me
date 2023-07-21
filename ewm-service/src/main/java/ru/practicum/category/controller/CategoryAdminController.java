package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.data.dto.CategoryDto;
import ru.practicum.category.data.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        CategoryDto dto = categoryService.createCategory(newCategoryDto);
        log.info(String.format("%s: category created: %s", LocalDateTime.now(), dto));
        return dto;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive long catId) {
        categoryService.deleteCategory(catId);
        log.info(String.format("%s: category was deleted by id: %d", LocalDateTime.now(), catId));
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable long catId) {
        CategoryDto dto = categoryService.patchCategory(categoryDto, catId);
        log.info(String.format("%s: category was patched: %s", LocalDateTime.now(), dto));
        return dto;
    }
}