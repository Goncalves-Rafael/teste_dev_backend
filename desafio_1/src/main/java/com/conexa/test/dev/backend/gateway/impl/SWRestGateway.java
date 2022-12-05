package com.conexa.test.dev.backend.gateway.impl;

import com.conexa.test.dev.backend.config.ConfigProperties;
import com.conexa.test.dev.backend.exception.SWApiException;
import com.conexa.test.dev.backend.gateway.SWDataSourceGateway;
import com.conexa.test.dev.backend.response.FilmResponse;
import com.conexa.test.dev.backend.response.PageResponse;
import com.conexa.test.dev.backend.response.PersonResponse;
import com.conexa.test.dev.backend.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

@Component
@ConditionalOnProperty(name = "swApiConfig.mock", havingValue = "false")
public class SWRestGateway implements SWDataSourceGateway {

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private HttpUtils httpUtils;

    public PageResponse<List<PersonResponse>> getPersonsByName(String personName) throws SWApiException {
        URI uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(configProperties.getSwApiConfig().getApiUrl()))
                .path("/people/")
                .queryParam("search", personName)
                .build().toUri();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        return httpUtils.performRequest(httpRequest, new TypeReference<PageResponse<List<PersonResponse>>>() {});
    }

    @Override
    public FilmResponse getFilmByUrl(String url) throws SWApiException {
        URI uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(url))
                .build().toUri();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        return httpUtils.performRequest(httpRequest, new TypeReference<FilmResponse>() {});
    }
}
