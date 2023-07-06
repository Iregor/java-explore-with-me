package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewStats {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @PositiveOrZero
    private long hits;
}