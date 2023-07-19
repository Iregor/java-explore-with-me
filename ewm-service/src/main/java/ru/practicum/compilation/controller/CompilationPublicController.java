package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.data.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilationsByParams(@RequestParam(required = false) Boolean pinned, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        List<CompilationDto> compilations = compilationService.getCompilationsByParams(pinned, from, size);
        log.info("{}: compilations were returned: {}", LocalDateTime.now(), compilations.toString());
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Positive long compId) {
        CompilationDto compilationDto = compilationService.getCompilationById(compId);
        log.info("{}: compilation returned: {}", LocalDateTime.now(), compilationDto);
        return compilationDto;
    }
}
