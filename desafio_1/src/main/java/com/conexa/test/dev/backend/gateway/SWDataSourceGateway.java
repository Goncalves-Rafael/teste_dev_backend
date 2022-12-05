package com.conexa.test.dev.backend.gateway;

import com.conexa.test.dev.backend.exception.SWApiException;
import com.conexa.test.dev.backend.response.FilmResponse;
import com.conexa.test.dev.backend.response.PageResponse;
import com.conexa.test.dev.backend.response.PersonResponse;

import java.util.List;

public interface SWDataSourceGateway {
    public PageResponse<List<PersonResponse>> getPersonsByName(String personName) throws SWApiException;
    public FilmResponse getFilmByUrl(String url) throws SWApiException;
}
