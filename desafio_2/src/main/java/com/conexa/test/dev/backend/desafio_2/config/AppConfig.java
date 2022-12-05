package com.conexa.test.dev.backend.desafio_2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpClient;
import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    private final ObjectMapper mapper;

    @Autowired
    public AppConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                .filter(x -> x instanceof MappingJackson2HttpMessageConverter)
                .forEach(x -> ((MappingJackson2HttpMessageConverter) x).setObjectMapper(mapper));
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

}
