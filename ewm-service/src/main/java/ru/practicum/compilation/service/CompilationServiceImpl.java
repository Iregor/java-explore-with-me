package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.data.Compilation;
import ru.practicum.compilation.data.CompilationDto;
import ru.practicum.compilation.data.CompilationMapper;
import ru.practicum.compilation.data.CompilationRepository;
import ru.practicum.compilation.data.dto.NewCompilationDto;
import ru.practicum.compilation.data.dto.UpdateCompilationRequest;
import ru.practicum.event.data.Event;
import ru.practicum.event.data.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = newCompilationDto.getEvents() != null ?
                eventRepository.findAllByIdIn(newCompilationDto.getEvents()) : null;
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow();
        List<Event> events;
        if (updateCompilationRequest.getEvents() != null && updateCompilationRequest.getEvents().size() > 0) {
            events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
        } else {
            events = compilation.getEvents();
        }
        compilation = CompilationMapper.toCompilation(updateCompilationRequest, compilation, events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilationsByParams(Boolean pinned, int from, int size) {
        List<Compilation> compilations = pinned != null ?
                compilationRepository.findAllByPinned(pinned, getPageable(from, size)) :
                compilationRepository.findAll(getPageable(from, size)).stream().collect(Collectors.toList());
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow());
    }

    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size, Sort.by("id").ascending());
    }
}
