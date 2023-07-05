package ru.practicum;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String statServerUrl;

    public StatClient(String statServerUrl) {
        this.statServerUrl = statServerUrl;
    }

    public void saveEndpointHit(String app, String uri, String ip, String timestamp) {
        EndpointHitDto dto = new EndpointHitDto(app, uri, ip, timestamp);
        HttpEntity<EndpointHitDto> httpEntity = new HttpEntity<>(dto, defaultHeaders());
        restTemplate.exchange(statServerUrl + "/hit", HttpMethod.POST, httpEntity, Void.class);
    }

    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String endpointPath = buildURI(start, end, uris, unique);
        Map<String, Object> parameters = Map.of("start", start, "end", end, "unique", unique);
        HttpEntity<EndpointHitDto> httpEntity = new HttpEntity<>(defaultHeaders());
        return restTemplate.exchange(statServerUrl + endpointPath, HttpMethod.GET, httpEntity, Object.class, parameters);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private String buildURI(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        StringBuilder sb = new StringBuilder();
        sb.append("/stats?start={start}&end={end}");
        for (String uri : uris) {
            sb.append(String.format("&uris={%s}", uri));
        }
        sb.append("&unique={unique}");
        return sb.toString();
    }
}
