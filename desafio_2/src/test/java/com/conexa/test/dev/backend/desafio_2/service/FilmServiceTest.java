package com.conexa.test.dev.backend.desafio_2.service;

import com.conexa.test.dev.backend.desafio_2.entity.FilmEntity;
import com.conexa.test.dev.backend.desafio_2.repository.FilmRepository;
import com.conexa.test.dev.backend.desafio_2.response.FilmResponse;
import com.conexa.test.dev.backend.desafio_2.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private HttpUtils httpUtils;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private CollectionModel<FilmResponse> filmsResponse;

    @Mock
    private List<FilmResponse> filmsContent;

    private FilmService filmService;

    @BeforeEach
    void init() {
        filmService = new FilmService(httpUtils, filmRepository, objectMapper);
    }

    @Test
    void shouldCallHttpUtilsOnlyOnceWhenPageDoesntHaveNext() throws IOException, InterruptedException {
        when(httpUtils.executeGetAsString(anyString())).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("");
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(filmsResponse);
        when(filmsResponse.getContent()).thenReturn(filmsContent);
        List<FilmEntity> filmEntities = Collections.singletonList(new FilmEntity());
        when(objectMapper.convertValue(eq(filmsContent), any(TypeReference.class))).thenReturn(filmEntities);
        when(filmRepository.saveAll(filmEntities)).thenReturn(filmEntities);

        filmService.loadFilmsFromApiIntoDatabase();

        verify(httpUtils, times(1)).executeGetAsString(anyString());
        verify(filmRepository, times(1)).saveAll(any());
    }

    @Test
    void shouldCallHttpUtilsAgainWhenPageHaveNext() throws IOException, InterruptedException {
        when(httpUtils.executeGetAsString(anyString())).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("");
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(filmsResponse);
        when(filmsResponse.getContent()).thenReturn(filmsContent);
        Optional<Link> optionalLink = Optional.of(Link.of("href", "next"));
        when(filmsResponse.getLink("next")).thenReturn(optionalLink, optionalLink, Optional.empty());
        List<FilmEntity> filmEntities = Collections.singletonList(new FilmEntity());
        when(objectMapper.convertValue(eq(filmsContent), any(TypeReference.class))).thenReturn(filmEntities);
        when(filmRepository.saveAll(filmEntities)).thenReturn(filmEntities);

        filmService.loadFilmsFromApiIntoDatabase();

        verify(httpUtils, times(2)).executeGetAsString(anyString());
        verify(filmRepository, times(2)).saveAll(any());
    }

    @Test
    void shouldInsertNewFilmIntoDatabase() throws IOException {
        Resource resource = mock(Resource.class);
        ReflectionTestUtils.setField(filmService, "mockFilmFile", resource);
        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(new FilmEntity());
        when(filmRepository.save(any())).thenReturn(new FilmEntity());

        filmService.insertCustomFilmIntoDatabase();

        verify(filmRepository, times(1)).save(any());
    }

}