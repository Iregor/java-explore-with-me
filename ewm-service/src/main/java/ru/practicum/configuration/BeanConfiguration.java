package ru.practicum.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    StatClient createStatClient() {
        return new StatClient();
    }
}
