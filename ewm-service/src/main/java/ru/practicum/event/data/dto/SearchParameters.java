package ru.practicum.event.data.dto;

import lombok.Getter;
import lombok.ToString;
import ru.practicum.event.data.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class SearchParameters {
    private String text;
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Integer from;
    private Integer size;
    private SortProperty sortProperty;


    public SearchParameters setSortProperty(SortProperty sortProperty) {
        this.sortProperty = sortProperty;
        return this;
    }

    public SearchParameters setUsers(List<Long> users) {
        this.users = users;
        return this;
    }

    public SearchParameters setText(String text) {
        this.text = text;
        return this;
    }

    public SearchParameters setPaid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public SearchParameters setOnlyAvailable(Boolean onlyAvailable) {
        this.onlyAvailable = onlyAvailable;
        return this;
    }

    public SearchParameters setStates(List<EventState> states) {
        this.states = states;
        return this;
    }

    public SearchParameters setCategories(List<Long> categories) {
        this.categories = categories;
        return this;
    }

    public SearchParameters setRangeStart(LocalDateTime rangeStart) {
        this.rangeStart = rangeStart;
        return this;
    }

    public SearchParameters setRangeEnd(LocalDateTime rangeEnd) {
        this.rangeEnd = rangeEnd;
        return this;
    }

    public SearchParameters setFrom(int from) {
        this.from = from;
        return this;
    }

    public SearchParameters setSize(int size) {
        this.size = size;
        return this;
    }
}
