package ru.practicum.event.data.dto;

import lombok.Data;
import ru.practicum.request.data.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
