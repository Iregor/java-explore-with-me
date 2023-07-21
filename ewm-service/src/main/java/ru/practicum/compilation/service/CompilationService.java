package ru.practicum.compilation.service;

import ru.practicum.compilation.data.CompilationDto;
import ru.practicum.compilation.data.dto.NewCompilationDto;
import ru.practicum.compilation.data.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilationsByParams(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(long compId);
}
