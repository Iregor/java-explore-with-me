package ru.practicum.compilation.data;

import ru.practicum.compilation.data.dto.NewCompilationDto;
import ru.practicum.compilation.data.dto.UpdateCompilationRequest;
import ru.practicum.event.data.Event;
import ru.practicum.event.data.dto.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .events(events)
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest, Compilation compilation, List<Event> events) {
        return Compilation.builder()
                .id(compilation.getId())
                .title(updateCompilationRequest.getTitle() != null ? updateCompilationRequest.getTitle() : compilation.getTitle())
                .pinned(updateCompilationRequest.getPinned() != null ? updateCompilationRequest.getPinned() : compilation.isPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(compilation.getEvents() != null ? compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()) : null)
                .build();
    }
}
