package com.conexa.test.dev.backend.desafio_2.service;

import com.conexa.test.dev.backend.desafio_2.constant.AppConstant;
import com.conexa.test.dev.backend.desafio_2.entity.FilmEntity;
import com.conexa.test.dev.backend.desafio_2.repository.FilmRepository;
import com.conexa.test.dev.backend.desafio_2.response.FilmResponse;
import com.conexa.test.dev.backend.desafio_2.utils.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static com.conexa.test.dev.backend.desafio_2.constant.AppConstant.*;

@Service
public class FilmService {
    private static final Logger LOG = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:mockFilmResponse.json")
    private Resource mockFilmFile;

    @Value("${config.rest.baseUrl}")
    private String restApiBaseUrl;


    /**
     * This method is responsible from retrieving all films from Challenge 1 Rest API then persisting them into H2 Database
     * Data retrieval process is iterative, going through all available pages while there is a next page to go to
     * @return int - total count of saved films
     * @throws IOException
     * @throws InterruptedException
     */
    public int loadFilmsFromApiIntoDatabase() throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = httpUtils.executeGetAsString(restApiBaseUrl + LUKE_FILMS_PATH);
        CollectionModel<FilmResponse> films = objectMapper.readValue(httpResponse.body(), new TypeReference<>() {});
        List<FilmEntity> filmEntities = mapResponseAndSave(films);
        int currentPage = 1;
        int total = filmEntities.size();
        printCurrentSavedPage(currentPage, filmEntities);

        while (films.getLink("next").isPresent()) {
            httpResponse = httpUtils.executeGetAsString(films.getLink("next").get().getHref());
            films = objectMapper.readValue(httpResponse.body(), new TypeReference<>() {});
            filmEntities = mapResponseAndSave(films);
            currentPage++;
            printCurrentSavedPage(currentPage, filmEntities);
            total += filmEntities.size();
        }
        return total;
    }

    public void insertCustomFilmIntoDatabase() throws IOException {
        FilmEntity filmEntity = objectMapper.readValue(mockFilmFile.getInputStream(), new TypeReference<>() {});
        filmEntity.setIsNew(Boolean.TRUE);
        printFilm(filmRepository.save(filmEntity));
    }

    public void printAllSavedFilms() {
        LOG.info("FilmService::printAllSavedFilms -> Start");
        List<FilmEntity> filmEntities = filmRepository.findAll();
        for (FilmEntity filmEntity : filmEntities) {
            printFilm(filmEntity);
        }
        LOG.info("FilmService::printAllSavedFilms -> End");
    }

    private List<FilmEntity> mapResponseAndSave(CollectionModel<FilmResponse> films) {
        List<FilmEntity> filmEntities = objectMapper.convertValue(films.getContent(), new TypeReference<>() {});
        return filmRepository.saveAll(filmEntities);
    }

    private void printCurrentSavedPage(int currentPage, List<FilmEntity> filmEntities) {
        LOG.info(AppConstant.LogStrings.LOG_PAGE_DATA, currentPage);
        for (FilmEntity filmEntity : filmEntities) {
            printFilm(filmEntity);
        }
    }

    private void printFilm(FilmEntity filmEntity) {
        LOG.info(AppConstant.LogStrings.LOG_FILM_DATA, filmEntity.getTitle(), filmEntity.getDirector(), filmEntity.getEpisodeId(), filmEntity.getReleaseDate(), filmEntity.getIsNew());
    }
}
