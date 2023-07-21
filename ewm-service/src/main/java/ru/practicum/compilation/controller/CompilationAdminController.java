package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.data.CompilationDto;
import ru.practicum.compilation.data.dto.NewCompilationDto;
import ru.practicum.compilation.data.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        CompilationDto compilationDto = compilationService.createCompilation(newCompilationDto);
        log.info("{}: compilation created: {}", LocalDateTime.now(), compilationDto.toString());
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive long compId) {
        compilationService.deleteCompilation(compId);
        log.info("{}: compilation with id={} was deleted.", LocalDateTime.now(), compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable @Positive long compId, @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        CompilationDto compilationDto = compilationService.patchCompilation(compId, updateCompilationRequest);
        log.info("{}: compilation updated {}", LocalDateTime.now(), compilationDto);
        return compilationDto;
    }


}
