package com.conexa.test.dev.backend.gateway.impl;

import com.conexa.test.dev.backend.exception.SWApiException;
import com.conexa.test.dev.backend.gateway.SWDataSourceGateway;
import com.conexa.test.dev.backend.response.FilmResponse;
import com.conexa.test.dev.backend.response.PageResponse;
import com.conexa.test.dev.backend.response.PersonResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@ConditionalOnProperty(name = "swApiConfig.mock", havingValue = "true")
public class SWMockGateway implements SWDataSourceGateway {

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:mockData/mockPersonResponse.json")
    Resource mockPersonFile;

    @Value("classpath:mockData/mockFilmResponse.json")
    Resource mockFilmFile;

    @Override
    public PageResponse<List<PersonResponse>> getPersonsByName(String personName) throws SWApiException {
        try {
            return mapper.readValue(mockPersonFile.getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new SWApiException(e.getMessage(), e, HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public FilmResponse getFilmByUrl(String url) throws SWApiException {
        try {
            return mapper.readValue(mockFilmFile.getInputStream(), FilmResponse.class);
        } catch (IOException e) {
            throw new SWApiException(e.getMessage(), e, HttpStatus.BAD_GATEWAY);
        }
    }
}
