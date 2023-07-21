package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class StatClient {
    public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${stat.url}")
    private String statServerUrl;

    public void saveEndpointHit(String app, String uri, String ip, String timestamp) {
        EndpointHitDto dto = new EndpointHitDto(app, uri, ip, timestamp);
        HttpEntity<EndpointHitDto> httpEntity = new HttpEntity<>(dto, defaultHeaders());
        restTemplate.exchange(statServerUrl + "/hit", HttpMethod.POST, httpEntity, Void.class);
    }

    public ResponseEntity<ViewStats[]> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String endpointPath = buildURI(uris);
        Map<String, Object> parameters = Map.of("start", start.format(dateTimeFormatter), "end", end.format(dateTimeFormatter), "unique", unique);
        HttpEntity<EndpointHitDto> httpEntity = new HttpEntity<>(defaultHeaders());
        return restTemplate.exchange(statServerUrl + endpointPath, HttpMethod.GET, httpEntity, ViewStats[].class, parameters);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private String buildURI(List<String> uris) {
        StringBuilder sb = new StringBuilder();
        sb.append("/stats?start={start}&end={end}");
        for (String uri : uris) {
            sb.append(String.format("&uris=%s", uri));
        }
        sb.append("&unique={unique}");
        return sb.toString();
    }
}
